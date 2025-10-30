package bike.rental.system.view;

import bike.rental.system.controller.*;
import bike.rental.system.model.*;
import bike.rental.system.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

public class AdminDashboardView extends JFrame {
    private final int adminId;
    private JTabbedPane tabs;

    // Controllers
    private final BikeController bikeCtrl = new BikeController();
    // private final RentalController rentalCtrl = new RentalController();
    private final MessageController msgCtrl = new MessageController();
    private final PromoController promoCtrl = new PromoController();
    private final TicketController ticketCtrl = new TicketController();

    public AdminDashboardView(int adminId) throws SQLException {
        this.adminId = adminId;
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();
        add(tabs);

        addOverviewTab();
        addBikeManagementTab();
        addPendingRentalsTab();
        addMessageTab();
        addPromoTab();
        addMaintenanceTab();
        addFeedbackTab();
        addWalletTab();
        addBroadcastTab();
        addTicketTab();

        setVisible(true);
        System.out.println("Admin Dashboard loaded successfully.");
    }

    /* --------------------------------------------------------------
       1. Overview (your original stats)
       -------------------------------------------------------------- */
    private void addOverviewTab() throws SQLException {
        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel l1 = new JLabel("Total Rentals:");   JLabel v1 = new JLabel();
        JLabel l2 = new JLabel("Total Revenue:");   JLabel v2 = new JLabel();
        JLabel l3 = new JLabel("Available Bikes:"); JLabel v3 = new JLabel();

        p.add(l1); p.add(v1);
        p.add(l2); p.add(v2);
        p.add(l3); p.add(v3);

        // fetch values
        String sqlRent = "SELECT COUNT(*) FROM rentals";
        String sqlRev  = "SELECT SUM(total_cost) FROM rentals";
        String sqlBike = "SELECT COUNT(*) FROM bikes WHERE status='available'";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps1 = c.prepareStatement(sqlRent);
             PreparedStatement ps2 = c.prepareStatement(sqlRev);
             PreparedStatement ps3 = c.prepareStatement(sqlBike);
             ResultSet rs1 = ps1.executeQuery();
             ResultSet rs2 = ps2.executeQuery();
             ResultSet rs3 = ps3.executeQuery()) {

            rs1.next(); v1.setText(String.valueOf(rs1.getInt(1)));
            rs2.next(); v2.setText(String.format("%.2f", rs2.getDouble(1)));
            rs3.next(); v3.setText(String.valueOf(rs3.getInt(1)));
        }

        tabs.addTab("Overview", p);
    }

    /* --------------------------------------------------------------
       2. Bike Management (your existing add / edit / delete)
       -------------------------------------------------------------- */
    private void addBikeManagementTab() {
        JPanel panel = new JPanel(new GridLayout(15, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField brandF = new JTextField(12), modelF = new JTextField(12), priceF = new JTextField(12);
        JTextField delIdF = new JTextField(12);
        JTextField editIdF = new JTextField(12), eBrandF = new JTextField(12), eModelF = new JTextField(12), ePriceF = new JTextField(12);
        JTextArea listArea = new JTextArea(6, 30); listArea.setEditable(false);
        JScrollPane sp = new JScrollPane(listArea);

        panel.add(new JLabel("Add Brand:")); panel.add(brandF);
        panel.add(new JLabel("Add Model:")); panel.add(modelF);
        panel.add(new JLabel("Add Price/hr:")); panel.add(priceF);
        JButton addBtn = new JButton("Add Bike");
        addBtn.addActionListener(e -> {
            try {
                double price = Double.parseDouble(priceF.getText().trim());
                if (addBike(brandF.getText().trim(), modelF.getText().trim(), price)) {
                    JOptionPane.showMessageDialog(this, "Bike added!");
                    refreshBikeList(listArea);
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        panel.add(addBtn);

        panel.add(new JLabel("Delete Bike ID:")); panel.add(delIdF);
        JButton delBtn = new JButton("Delete");
        delBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(delIdF.getText().trim());
                if (deleteBike(id)) {
                    JOptionPane.showMessageDialog(this, "Deleted!");
                    refreshBikeList(listArea);
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        panel.add(delBtn);

        panel.add(new JLabel("Edit Bike ID:")); panel.add(editIdF);
        panel.add(new JLabel("New Brand:")); panel.add(eBrandF);
        panel.add(new JLabel("New Model:")); panel.add(eModelF);
        panel.add(new JLabel("New Price/hr:")); panel.add(ePriceF);
        JButton editBtn = new JButton("Update");
        editBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(editIdF.getText().trim());
                double p = Double.parseDouble(ePriceF.getText().trim());
                if (updateBike(id, eBrandF.getText().trim(), eModelF.getText().trim(), p)) {
                    JOptionPane.showMessageDialog(this, "Updated!");
                    refreshBikeList(listArea);
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        panel.add(editBtn);

        panel.add(new JLabel("All Bikes:")); panel.add(sp);
        JButton refBtn = new JButton("Refresh List");
        refBtn.addActionListener(e -> refreshBikeList(listArea));
        panel.add(refBtn);

        refreshBikeList(listArea);
        tabs.addTab("Bike Management", panel);
    }

    private boolean addBike(String brand, String model, double price) throws SQLException {
        String sql = "INSERT INTO bikes (brand, model, price_per_hour, status) VALUES (?,?,?,'available')";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, brand); ps.setString(2, model); ps.setDouble(3, price);
            return ps.executeUpdate() > 0;
        }
    }

    private boolean deleteBike(int id) throws SQLException {
        String sql = "DELETE FROM bikes WHERE bike_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private boolean updateBike(int id, String brand, String model, double price) throws SQLException {
        String sql = "UPDATE bikes SET brand=?, model=?, price_per_hour=? WHERE bike_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, brand); ps.setString(2, model); ps.setDouble(3, price); ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    private void refreshBikeList(JTextArea area) {
        try {
            String sql = "SELECT bike_id, brand, model, price_per_hour, status FROM bikes";
            try (Connection c = DatabaseConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("ID:").append(rs.getInt("bike_id"))
                      .append(" | ").append(rs.getString("brand"))
                      .append(" ").append(rs.getString("model"))
                      .append(" | ₹").append(rs.getDouble("price_per_hour"))
                      .append("/hr | ").append(rs.getString("status")).append("\n");
                }
                area.setText(sb.toString());
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /* --------------------------------------------------------------
       3. Pending Rental Approvals
       -------------------------------------------------------------- */
    private void addPendingRentalsTab() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"RentalID","UserID","BikeID","Start","End","Cost","Fine"},0);
        JTable table = new JTable(tm);
        JScrollPane sp = new JScrollPane(table);
        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadPendingRentals(tm));
        p.add(refresh, BorderLayout.SOUTH);
        loadPendingRentals(tm);

        // Approve / Reject buttons (selected row)
        JPanel btns = new JPanel();
        JButton approve = new JButton("Approve");
        JButton reject = new JButton("Reject");
        approve.addActionListener(e -> changeRentalStatus(table, tm, "active"));
        reject.addActionListener(e -> changeRentalStatus(table, tm, "rejected"));
        btns.add(approve); btns.add(reject);
        p.add(btns, BorderLayout.NORTH);

        tabs.addTab("Pending Rentals", p);
    }

    private void loadPendingRentals(DefaultTableModel tm) {
        tm.setRowCount(0);
        String sql = "SELECT rental_id,user_id,bike_id,start_time,end_time,total_cost,fine_amount FROM rentals WHERE status='pending'";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tm.addRow(new Object[]{
                    rs.getInt("rental_id"),
                    rs.getInt("user_id"),
                    rs.getInt("bike_id"),
                    rs.getTimestamp("start_time"),
                    rs.getTimestamp("end_time"),
                    rs.getDouble("total_cost"),
                    rs.getDouble("fine_amount")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void changeRentalStatus(JTable table, DefaultTableModel tm, String newStatus) {
        int row = table.getSelectedRow();
        if (row == -1) { 
            JOptionPane.showMessageDialog(this, "Select a row");
            return; }
        int rentalId = (int) tm.getValueAt(row, 0);
        String sql = "UPDATE rentals SET status = ? WHERE rental_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, rentalId);
            ps.executeUpdate();
            if ("active".equals(newStatus)) {
                int bikeId = (int) tm.getValueAt(row, 2);
                bikeCtrl.updateBikeStatus(bikeId, "unavailable");
            }
            loadPendingRentals(tm);
            JOptionPane.showMessageDialog(this, "Rental " + newStatus + "!");
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /* --------------------------------------------------------------
       4. Messaging
       -------------------------------------------------------------- */
    private void addMessageTab() {
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
                msgCtrl.sendMessage(adminId, to, txt.getText());
                txt.setText("");
                JOptionPane.showMessageDialog(this, "Message sent!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid input"); }
        });

        int messagesTabIndex = tabs.getTabCount(); 
        
        tabs.addTab("Messages", p);
        
        Timer t = new Timer(5000, e -> {
            try {
                List<Message> unread = msgCtrl.getUnreadForUser(adminId);
                int cnt = unread.size();
                int currentTabIndex = tabs.indexOfTab("Messages");
                if (currentTabIndex != -1) {
                    tabs.setTitleAt(currentTabIndex, "Messages (" + cnt + ")");
                }
            } catch (Exception ex) {
            }
        });
        t.start();
    }

    /* --------------------------------------------------------------
       5. Promo Codes
       -------------------------------------------------------------- */
    private void addPromoTab() {
        JPanel p = new JPanel(new GridLayout(6,2,10,10));
        JTextField codeF = new JTextField(), discF = new JTextField(), fromF = new JTextField("yyyy-mm-dd"),
                  toF = new JTextField("yyyy-mm-dd"), usesF = new JTextField();
        p.add(new JLabel("Code:")); p.add(codeF);
        p.add(new JLabel("% Discount:")); p.add(discF);
        p.add(new JLabel("Valid From:")); p.add(fromF);
        p.add(new JLabel("Valid To:")); p.add(toF);
        p.add(new JLabel("Uses (blank=∞):")); p.add(usesF);
        JButton create = new JButton("Create Promo");
        create.addActionListener(e -> {
            try {
                double disc = Double.parseDouble(discF.getText().trim());
                java.sql.Date f = fromF.getText().isEmpty() ? null : java.sql.Date.valueOf(fromF.getText().trim());
                java.sql.Date t = toF.getText().isEmpty() ? null : java.sql.Date.valueOf(toF.getText().trim());
                Integer uses = usesF.getText().isEmpty() ? null : Integer.parseInt(usesF.getText().trim());
                promoCtrl.createPromo(codeF.getText().trim(), disc, f, t, uses);
                JOptionPane.showMessageDialog(this, "Promo created!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        p.add(create);
        tabs.addTab("Promo Codes", p);
    }

    /* --------------------------------------------------------------
       6. Maintenance
       -------------------------------------------------------------- */
    private void addMaintenanceTab() {
        JPanel p = new JPanel(new GridLayout(5,2,10,10));
        JTextField bikeIdF = new JTextField(), reasonF = new JTextField(),
                  startF = new JTextField("yyyy-mm-dd"), endF = new JTextField("yyyy-mm-dd");
        p.add(new JLabel("Bike ID:")); p.add(bikeIdF);
        p.add(new JLabel("Reason:")); p.add(reasonF);
        p.add(new JLabel("Start Date:")); p.add(startF);
        p.add(new JLabel("End Date:")); p.add(endF);
        JButton mark = new JButton("Mark Maintenance");
        mark.addActionListener(e -> {
            try {
                int bikeId = Integer.parseInt(bikeIdF.getText().trim());
                java.sql.Date s = java.sql.Date.valueOf(startF.getText().trim());
                java.sql.Date en = endF.getText().isEmpty() ? null : java.sql.Date.valueOf(endF.getText().trim());
                String sql = "INSERT INTO maintenance_log (bike_id, reason, start_date, end_date) VALUES (?,?,?,?)";
                try (Connection c = DatabaseConnection.getConnection();
                     PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, bikeId);
                    ps.setString(2, reasonF.getText().trim());
                    ps.setDate(3, s);
                    if (en == null) ps.setNull(4, Types.DATE); else ps.setDate(4, en);
                    ps.executeUpdate();
                    bikeCtrl.updateBikeStatus(bikeId, "maintenance");
                    JOptionPane.showMessageDialog(this, "Bike marked for maintenance");
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        p.add(mark);
        tabs.addTab("Maintenance", p);
    }

    /* --------------------------------------------------------------
       7. Feedback (average per bike)
       -------------------------------------------------------------- */
    private void addFeedbackTab() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"BikeID","Avg Rating","Comments"},0);
        JTable tbl = new JTable(tm);
        JScrollPane sp = new JScrollPane(tbl);
        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);
        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> loadFeedback(tm));
        p.add(ref, BorderLayout.SOUTH);
        loadFeedback(tm);
        tabs.addTab("Feedback", p);
    }

    private void loadFeedback(DefaultTableModel tm) {
        tm.setRowCount(0);
        String sql = "SELECT b.bike_id, AVG(f.rating) avg_rating, COUNT(f.feedback_id) cnt " +
                     "FROM bikes b LEFT JOIN rentals r ON b.bike_id=r.bike_id " +
                     "LEFT JOIN feedback f ON r.rental_id=f.rental_id " +
                     "GROUP BY b.bike_id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tm.addRow(new Object[]{
                    rs.getInt("bike_id"),
                    String.format("%.2f", rs.getDouble("avg_rating")),
                    rs.getInt("cnt") + " comment(s)"
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /* --------------------------------------------------------------
       8. Wallet Management
       -------------------------------------------------------------- */
    private void addWalletTab() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"UserID","Name","Balance"},0);
        JTable tbl = new JTable(tm);
        JScrollPane sp = new JScrollPane(tbl);
        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);

        JPanel top = new JPanel();
        JTextField uidF = new JTextField(5), amtF = new JTextField(8);
        JButton add = new JButton("Add Money");
        top.add(new JLabel("User ID:")); top.add(uidF);
        top.add(new JLabel("Amount:")); top.add(amtF);
        top.add(add);
        p.add(top, BorderLayout.NORTH);

        add.addActionListener(e -> {
            try {
                int uid = Integer.parseInt(uidF.getText().trim());
                double amt = Double.parseDouble(amtF.getText().trim());
                String sql = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
                try (Connection c = DatabaseConnection.getConnection();
                     PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setDouble(1, amt);
                    ps.setInt(2, uid);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "₹" + amt + " added!");
                    loadWallets(tm);
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> loadWallets(tm));
        p.add(ref, BorderLayout.SOUTH);
        loadWallets(tm);
        tabs.addTab("Wallets", p);
    }

    private void loadWallets(DefaultTableModel tm) {
        tm.setRowCount(0);
        String sql = "SELECT user_id, name, balance FROM users WHERE role='customer'";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tm.addRow(new Object[]{rs.getInt("user_id"), rs.getString("name"), rs.getDouble("balance")});
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /* --------------------------------------------------------------
       9. Broadcast
       -------------------------------------------------------------- */
    private void addBroadcastTab() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea txt = new JTextArea(4,40);
        JButton send = new JButton("Broadcast");
        send.addActionListener(e -> {
            String msg = txt.getText().trim();
            if (!msg.isEmpty()) {
                String sql = "INSERT INTO broadcasts (text) VALUES (?)";
                try (Connection c = DatabaseConnection.getConnection();
                     PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, msg);
                    ps.executeUpdate();
                    txt.setText("");
                    JOptionPane.showMessageDialog(this, "Broadcast sent!");
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        });
        p.add(new JScrollPane(txt), BorderLayout.CENTER);
        p.add(send, BorderLayout.SOUTH);
        tabs.addTab("Broadcast", p);
    }

    /* --------------------------------------------------------------
       10. Support Tickets
       -------------------------------------------------------------- */
    private void addTicketTab() {
        DefaultTableModel tm = new DefaultTableModel(new String[]{"TicketID","UserID","Subject","Status"},0);
        JTable tbl = new JTable(tm);
        JScrollPane sp = new JScrollPane(tbl);
        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton view = new JButton("View Replies");
        JButton close = new JButton("Close Ticket");
        btns.add(view); btns.add(close);
        p.add(btns, BorderLayout.SOUTH);

        view.addActionListener(e -> showTicketReplies(tbl));
        close.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row != -1) {
                int tid = (int) tm.getValueAt(row, 0);
                ticketCtrl.closeTicket(tid);
                loadOpenTickets(tm);
            }
        });

        JButton ref = new JButton("Refresh");
        ref.addActionListener(e -> loadOpenTickets(tm));
        p.add(ref, BorderLayout.NORTH);
        loadOpenTickets(tm);
        tabs.addTab("Support Tickets", p);
    }

    private void loadOpenTickets(DefaultTableModel tm) {
        tm.setRowCount(0);
        List<Ticket> list = ticketCtrl.getOpenTicketsForAdmin();
        for (Ticket t : list) {
            tm.addRow(new Object[]{t.getTicketId(), t.getUserId(), t.getSubject(), t.getStatus()});
        }
    }

    private void showTicketReplies(JTable tbl) {
        int row = tbl.getSelectedRow();
        if (row == -1) return;
        int tid = (int) tbl.getModel().getValueAt(row, 0);
        List<TicketReply> replies = ticketCtrl.getReplies(tid);
        JTextArea area = new JTextArea(15,50);
        area.setEditable(false);
        StringBuilder sb = new StringBuilder();
        for (TicketReply r : replies) {
            sb.append(r.getUserId()).append(": ").append(r.getText()).append("\n");
        }
        area.setText(sb.toString());

        JPanel replyPanel = new JPanel(new BorderLayout());
        JTextArea newReply = new JTextArea(3,40);
        JButton send = new JButton("Send Reply");
        send.addActionListener(e -> {
            ticketCtrl.addReply(tid, adminId, newReply.getText());
            newReply.setText("");
            showTicketReplies(tbl);
        });
        replyPanel.add(new JScrollPane(newReply), BorderLayout.CENTER);
        replyPanel.add(send, BorderLayout.SOUTH);

        JPanel all = new JPanel(new BorderLayout());
        all.add(new JScrollPane(area), BorderLayout.CENTER);
        all.add(replyPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, all, "Ticket #" + tid, JOptionPane.PLAIN_MESSAGE);
    }

    /* --------------------------------------------------------------
       MAIN (for quick testing – remove later)
       -------------------------------------------------------------- */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new AdminDashboardView(1);   // adminId = 1
            } catch (SQLException e) { e.printStackTrace(); }
        });
    }
}