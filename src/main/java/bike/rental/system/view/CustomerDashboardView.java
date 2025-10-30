package bike.rental.system.view;

import bike.rental.system.controller.*;
import bike.rental.system.model.*;
import bike.rental.system.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

public class CustomerDashboardView extends JFrame {
    private final int userId;
    private JTabbedPane tabs;

    // Controllers
    private final BikeController bikeCtrl = new BikeController();
    private final RentalController rentalCtrl = new RentalController();
    private final MessageController msgCtrl = new MessageController();
    private final PromoController promoCtrl = new PromoController();
    private final TicketController ticketCtrl = new TicketController();

    public CustomerDashboardView(int userId) {
        this.userId = userId;
        setTitle("Customer Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();
        add(tabs);

        addBikeRentTab();
        addRentalHistoryTab();
        addMessagesTab();
        addFeedbackTab();
        addWalletTab();
        addTicketTab();

        // check broadcast on start
        checkBroadcast();

        setVisible(true);
        System.out.println("Customer Dashboard loaded for userId: " + userId);
    }

    /* --------------------------------------------------------------
       1. Rent a Bike (search + promo + wallet)
       -------------------------------------------------------------- */
    private void addBikeRentTab() {
        JPanel p = new JPanel(new BorderLayout());

        // search
        JPanel top = new JPanel();
        JTextField searchF = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        top.add(new JLabel("Search:")); top.add(searchF); top.add(searchBtn);
        p.add(top, BorderLayout.NORTH);

        // bike list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> bikeList = new JList<>(listModel);
        p.add(new JScrollPane(bikeList), BorderLayout.CENTER);

        // rent controls
        JPanel ctrl = new JPanel();
        JTextField hrsF = new JTextField(4);
        JTextField promoF = new JTextField(12);
        JLabel costLbl = new JLabel("Cost: ₹0.00");
        JButton rentBtn = new JButton("Rent");
        ctrl.add(new JLabel("Hours:")); ctrl.add(hrsF);
        ctrl.add(new JLabel("Promo:")); ctrl.add(promoF);
        ctrl.add(costLbl); ctrl.add(rentBtn);
        p.add(ctrl, BorderLayout.SOUTH);

        // load bikes
        searchBtn.addActionListener(e -> loadAvailableBikes(listModel, searchF.getText()));
        loadAvailableBikes(listModel, "");

        // cost recalc
        hrsF.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                recalcCost(bikeList, hrsF, promoF, costLbl);
            }


            @Override
            public void removeUpdate(DocumentEvent e) {
                recalcCost(bikeList, hrsF, promoF, costLbl);
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                recalcCost(bikeList, hrsF, promoF, costLbl);
            }
        });

        promoF.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                recalcCost(bikeList, hrsF, promoF, costLbl);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                recalcCost(bikeList, hrsF, promoF, costLbl);
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                recalcCost(bikeList, hrsF, promoF, costLbl);
            }
        });

        // Also recalc when bike selection changes
        bikeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                recalcCost(bikeList, hrsF, promoF, costLbl);
            }
        });

        // rent action
        rentBtn.addActionListener(e -> performRent(bikeList, hrsF, promoF, costLbl, listModel));

        tabs.addTab("Rent Bike", p);
    }

    private void loadAvailableBikes(DefaultListModel<String> model, String term) {
        model.clear();
        List<Bike> bikes = bikeCtrl.getAvailableBikes();
        term = term.toLowerCase();
        for (Bike b : bikes) {
            String info = b.getBikeId() + " | " + b.getBrand() + " " + b.getModel()
                    + " | ₹" + b.getPricePerHour() + "/hr";
            if (term.isEmpty() || info.toLowerCase().contains(term))
                model.addElement(info);
        }
    }

    private void recalcCost(JList<String> list, JTextField hrsF, JTextField promoF, JLabel costLbl) {
        int sel = list.getSelectedIndex();
        if (sel == -1) { costLbl.setText("Cost: ₹0.00"); return; }
        String line = list.getModel().getElementAt(sel);
        double priceHr = Double.parseDouble(line.split("₹")[1].split("/")[0].trim());
        try {
            int hrs = Integer.parseInt(hrsF.getText().trim());
            double base = priceHr * hrs;
            String code = promoF.getText().trim();
            double discount = 0;
            if (!code.isEmpty()) {
                PromoCode pc = promoCtrl.getValidPromo(code);
                if (pc != null) {
                    discount = base * pc.getDiscountPercent() / 100.0;
                    if (pc.getUsesLeft() != null) promoCtrl.decrementUses(code);
                }
            }
            costLbl.setText(String.format("Cost: ₹%.2f", base - discount));
        } catch (Exception ex) { costLbl.setText("Cost: ₹0.00"); }
    }

    private void performRent(JList<String> list, JTextField hrsF, JTextField promoF,
                            JLabel costLbl, DefaultListModel<String> bikeModel) {
        int sel = list.getSelectedIndex();
        if (sel == -1) { JOptionPane.showMessageDialog(this, "Select a bike"); return; }
        try {
            int hrs = Integer.parseInt(hrsF.getText().trim());
            if (hrs <= 0) throw new Exception();
            String line = list.getModel().getElementAt(sel);
            int bikeId = Integer.parseInt(line.split(" \\| ")[0]);
            double priceHr = Double.parseDouble(line.split("₹")[1].split("/")[0].trim());

            // promo
            double discount = 0;
            String code = promoF.getText().trim();
            PromoCode pc = null;
            if (!code.isEmpty()) {
                pc = promoCtrl.getValidPromo(code);
                if (pc != null) discount = priceHr * hrs * pc.getDiscountPercent() / 100.0;
            }

            double totalCost = priceHr * hrs - discount;

            // Check wallet balance
            double balance = getUserBalance();
            if (balance < totalCost) {
                JOptionPane.showMessageDialog(this, "Insufficient balance. Please add money to wallet.");
                return;
            }

            // Create rental
            if (rentalCtrl.createRental(userId, bikeId, hrs, totalCost, code)) {
                // Deduct from wallet
                updateWalletBalance(-totalCost);
                bikeCtrl.updateBikeStatus(bikeId, "unavailable");
                JOptionPane.showMessageDialog(this, "Bike rented successfully!");
                loadAvailableBikes(bikeModel, "");
                hrsF.setText("");
                promoF.setText("");
                costLbl.setText("Cost: ₹0.00");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to rent bike");
            }
        } catch (Exception ex) { 
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage()); 
        }
    }

    /* --------------------------------------------------------------
       2. Rental History
       -------------------------------------------------------------- */
    private void addRentalHistoryTab() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"RentalID","Bike","Start","End","Cost","Status"},0);
        JTable table = new JTable(tm);
        JScrollPane sp = new JScrollPane(table);
        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadRentalHistory(tm));
        p.add(refresh, BorderLayout.SOUTH);
        loadRentalHistory(tm);

        tabs.addTab("Rental History", p);
    }

    private void loadRentalHistory(DefaultTableModel tm) {
        tm.setRowCount(0);
        String sql = "SELECT r.rental_id, b.brand, b.model, r.start_time, r.end_time, r.total_cost, r.status " +
                     "FROM rentals r JOIN bikes b ON r.bike_id = b.bike_id " +
                     "WHERE r.user_id = ? ORDER BY r.start_time DESC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tm.addRow(new Object[]{
                    rs.getInt("rental_id"),
                    rs.getString("brand") + " " + rs.getString("model"),
                    rs.getTimestamp("start_time"),
                    rs.getTimestamp("end_time"),
                    rs.getDouble("total_cost"),
                    rs.getString("status")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /* --------------------------------------------------------------
       3. Messages
       -------------------------------------------------------------- */
    private void addMessagesTab() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> msgList = new JList<>(listModel);
        p.add(new JScrollPane(msgList), BorderLayout.CENTER);

        JPanel send = new JPanel();
        JTextField toFld = new JTextField(5);
        JTextArea txt = new JTextArea(3,30);
        JButton sendBtn = new JButton("Send");
        send.add(new JLabel("To User ID:")); send.add(toFld);
        send.add(new JScrollPane(txt)); send.add(sendBtn);
        p.add(send, BorderLayout.SOUTH);

        sendBtn.addActionListener(e -> {
            try {
                int to = Integer.parseInt(toFld.getText().trim());
                msgCtrl.sendMessage(userId, to, txt.getText());
                txt.setText("");
                JOptionPane.showMessageDialog(this, "Message sent!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid input"); }
        });

        // Load messages
        JButton refreshBtn = new JButton("Refresh Messages");
        refreshBtn.addActionListener(e -> loadMessages(listModel));
        p.add(refreshBtn, BorderLayout.NORTH);
        loadMessages(listModel);

        tabs.addTab("Messages", p);
    }

    private void loadMessages(DefaultListModel<String> model) {
        model.clear();
        List<Message> messages = msgCtrl.getMessagesForUser(userId);
        for (Message m : messages) {
            String info = "From: " + m.getFromUserId() + " | " + m.getText() + " | " + m.getSentAt();
            model.addElement(info);
        }
    }

    /* --------------------------------------------------------------
       4. Feedback
       -------------------------------------------------------------- */
    private void addFeedbackTab() {
        JPanel p = new JPanel(new GridLayout(5,2,10,10));
        JTextField rentalIdF = new JTextField();
        JTextField ratingF = new JTextField();
        JTextArea commentF = new JTextArea(3,20);
        p.add(new JLabel("Rental ID:")); p.add(rentalIdF);
        p.add(new JLabel("Rating (1-5):")); p.add(ratingF);
        p.add(new JLabel("Comment:")); p.add(new JScrollPane(commentF));
        JButton submit = new JButton("Submit Feedback");
        submit.addActionListener(e -> {
            try {
                int rentalId = Integer.parseInt(rentalIdF.getText().trim());
                int rating = Integer.parseInt(ratingF.getText().trim());
                if (rating < 1 || rating > 5) throw new Exception("Rating must be 1-5");
                String sql = "INSERT INTO feedback (rental_id, rating, comment) VALUES (?,?,?)";
                try (Connection c = DatabaseConnection.getConnection();
                     PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, rentalId);
                    ps.setInt(2, rating);
                    ps.setString(3, commentF.getText().trim());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Feedback submitted!");
                    rentalIdF.setText("");
                    ratingF.setText("");
                    commentF.setText("");
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        p.add(submit);
        tabs.addTab("Feedback", p);
    }

    /* --------------------------------------------------------------
       5. Wallet
       -------------------------------------------------------------- */
    private void addWalletTab() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel balanceLbl = new JLabel("Current Balance: ₹0.00", JLabel.CENTER);
        balanceLbl.setFont(new Font("Arial", Font.BOLD, 16));
        p.add(balanceLbl, BorderLayout.NORTH);

        JPanel addMoney = new JPanel();
        JTextField amtF = new JTextField(10);
        JButton addBtn = new JButton("Add Money");
        addMoney.add(new JLabel("Amount:")); addMoney.add(amtF); addMoney.add(addBtn);
        p.add(addMoney, BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(amtF.getText().trim());
                if (amt <= 0) throw new Exception();
                updateWalletBalance(amt);
                amtF.setText("");
                updateBalanceLabel(balanceLbl);
                JOptionPane.showMessageDialog(this, "₹" + amt + " added to wallet!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid amount"); }
        });

        updateBalanceLabel(balanceLbl);
        tabs.addTab("Wallet", p);
    }

    private void updateBalanceLabel(JLabel label) {
        double balance = getUserBalance();
        label.setText(String.format("Current Balance: ₹%.2f", balance));
    }

    private double getUserBalance() {
        String sql = "SELECT balance FROM users WHERE user_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 0;
    }

    private void updateWalletBalance(double amount) {
        String sql = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /* --------------------------------------------------------------
       6. Support Tickets
       -------------------------------------------------------------- */
    private void addTicketTab() {
        JPanel p = new JPanel(new BorderLayout());
        
        // Create ticket
        JPanel createPanel = new JPanel(new GridLayout(3,2,10,10));
        JTextField subjectF = new JTextField();
        JTextArea descF = new JTextArea(3,30);
        createPanel.add(new JLabel("Subject:")); createPanel.add(subjectF);
        createPanel.add(new JLabel("Description:")); createPanel.add(new JScrollPane(descF));
        JButton createBtn = new JButton("Create Ticket");
        createPanel.add(createBtn);
        p.add(createPanel, BorderLayout.NORTH);

        // Ticket list
        DefaultTableModel tm = new DefaultTableModel(new String[]{"TicketID","Subject","Status"},0);
        JTable tbl = new JTable(tm);
        p.add(new JScrollPane(tbl), BorderLayout.CENTER);

        createBtn.addActionListener(e -> {
            String subject = subjectF.getText().trim();
            String desc = descF.getText().trim();
            if (!subject.isEmpty() && !desc.isEmpty()) {
                ticketCtrl.createTicket(userId, subject, desc);
                subjectF.setText("");
                descF.setText("");
                loadUserTickets(tm);
                JOptionPane.showMessageDialog(this, "Ticket created!");
            }
        });

        JButton ref = new JButton("Refresh Tickets");
        ref.addActionListener(e -> loadUserTickets(tm));
        p.add(ref, BorderLayout.SOUTH);
        loadUserTickets(tm);

        tabs.addTab("Support Tickets", p);
    }

    private void loadUserTickets(DefaultTableModel tm) {
        tm.setRowCount(0);
        List<Ticket> tickets = ticketCtrl.getTicketsForUser(userId);
        for (Ticket t : tickets) {
            tm.addRow(new Object[]{t.getTicketId(), t.getSubject(), t.getStatus()});
        }
    }

    /* --------------------------------------------------------------
       Broadcast check
       -------------------------------------------------------------- */
    private void checkBroadcast() {
        String sql = "SELECT text FROM broadcasts ORDER BY created_at DESC LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Broadcast: " + rs.getString("text"));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    // Simple Document Listener interface
    interface SimpleDocumentListener {
        void update();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerDashboardView(1); // Test with user ID 1
        });
    }
}