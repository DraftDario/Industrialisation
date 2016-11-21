package ch.hearc.ig.odi.epicearc.business;

public enum ConiferType {

    SPRUCE("spruce"),
    NORDMANN("nordmann"),
    NOBILIS("nobilis");

    private String code;

    private ConiferType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
