package tpbeatiniortega

class Annonce {

    String title;
    String description;
    Date dateCreated;
    Date validTill;
    Boolean state;

    static belongsTo = [author: User]

    static hasMany = [illustrations: Illustration]

    static constraints = {
    }
}
