package com.suzanelsamahy.popularmovies1.classes;


public class Trailers {
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public Trailers() {

    }


    public String getName() { return name; }


    public void setName(String name) { this.name=name; }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }



    public String getTrailerID() {
        return id;
    }

    public void setTrailerID(String id) {
        this.id=id;
    }



    public String getSite() { return site; }

    public void setSite(String site) {
        this.site=site;
    }

    public String getType() {
        return type; }

    public  void setType( String type) { this.type=type; }
}
