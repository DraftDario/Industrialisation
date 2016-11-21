package ch.hearc.ig.odi.epicearc.business;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author julien.plumez
 */
public abstract class AbstractDate {

    private Long id;
    private Date date;
    private ConiferType coniferType;

    protected AbstractDate() {

    }

    protected AbstractDate(Long id, Date date, ConiferType coniferType) {
        this();

        this.id = id;
        this.date = date;
        this.coniferType = coniferType;
    }

    public String getFormatedDate() {
        return (new SimpleDateFormat("dd.MM.yyyy")).format(date);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ConiferType getConiferType() {
        return coniferType;
    }

    public void setConiferType(ConiferType coniferType) {
        this.coniferType = coniferType;
    }

    /**
     * Override it to get the specific date
     * @return the date formated 
     */
    @Override
    public String toString() {
        return getFormatedDate();
    }
}
