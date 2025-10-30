package bike.rental.system.model;
import java.sql.Date;

public class PromoCode {
    private String code;
    private double discountPercent;
    private Date validFrom, validTo;
    private Integer usesLeft;
    // getters / setters
    public String getCode() { return code; } public void setCode(String v) { code = v; }
    public double getDiscountPercent() { return discountPercent; } public void setDiscountPercent(double v) { discountPercent = v; }
    public Date getValidFrom() { return validFrom; } public void setValidFrom(Date v) { validFrom = v; }
    public Date getValidTo() { return validTo; } public void setValidTo(Date v) { validTo = v; }
    public Integer getUsesLeft() { return usesLeft; } public void setUsesLeft(Integer v) { usesLeft = v; }
}