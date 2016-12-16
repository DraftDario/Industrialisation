package ch.hearc.ig.odi.epicearc.business;

/**
 *
 * @author julien.plumez
 */
public class Product {
    private Long        id;
    private Float       price;
    private ConiferType coniferType;
    private ConiferSize coniferSize;

    public Product() {}

    public Product(Long id, Float price, ConiferType coniferType, ConiferSize coniferSize) {
        this.id          = id;
        this.price       = price;
        this.coniferType = coniferType;
        this.coniferSize = coniferSize;
    }

    /**
     * Override to let let know the user which product he want
     * @return  The formated String "size x price x".
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(coniferSize.toString().toLowerCase()).append(" size ").append(" ,  price : ").append(price);

        return sb.toString();
    }

    public ConiferSize getConiferSize() {
        return coniferSize;
    }

    public void setConiferSize(ConiferSize coniferSize) {
        this.coniferSize = coniferSize;
    }

    public ConiferType getConiferType() {
        return coniferType;
    }

    public void setConiferType(ConiferType coniferType) {
        this.coniferType = coniferType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
