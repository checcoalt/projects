package service;

public interface IMagazzino {
    
    public boolean deposita(String articolo, int id);
    public int preleva(String articolo);

}
