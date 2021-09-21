package sample.Data;

public class User {

    private String login="";
    private String pass="";
    private String group="";
    private Boolean is_block =false;
    public static enum UserType {
        Admin,
        User
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setIs_block(Boolean is_block) {
        this.is_block = is_block;
    }

    public User(){

    }
    public User(String login, String pass, UserType group ){
        this.login = login;
        this.pass = pass;
        this.group = group.name();
    }

    public Boolean getIs_block() {
        return is_block;
    }

    public String getGroup() {
        return group;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }
}
