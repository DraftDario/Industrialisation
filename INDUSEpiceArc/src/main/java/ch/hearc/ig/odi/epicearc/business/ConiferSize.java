package ch.hearc.ig.odi.epicearc.business;

public enum ConiferSize {

    MINI("mini"),
    SMALL("small"),
    MEDIUM("medium"),
    BIG("big");

    private String code;

    private ConiferSize(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
