package co.civicoapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;
class Delivery{
    public String open;
    public String closed;
    public String online;
    public String byphone;
}
class Offer{
    public String offer;
    public String info;
    public String normalPrice;
}
class Copie{
    public Delivery delivery;
    public Offer offer;
}
class Filter{
    public String name;
    public String value;
    public int count;
}
class Subcategorie{
    public String name;
    public String value;
    public boolean selected;
}
class Facet{
    public String name;
    public String slug;
    public String value;
    public String icon;
    public int count;
    public boolean selected;
    public List<Subcategorie> subcategories;
}
class Features{
    public String value;
    public String name;
    public int count;
    public boolean selected;
}
class Feature{
    public String name;
    public String slug;
    public List<Features> features;
}
class Genre{
    public String name;
    public String value;
    public boolean selected;
}
class Coordinate{
    public double lat;
    @SerializedName("long")
    public double long1;
}
class Result{
    public String id;
    public String name;
    public String name_html;
    public String image;
    public String type;
    public String description;
    public boolean small_business;
    public String category;
    public String category_slug;
    public String main_category;
    public String main_category_slug;
    public String location;
    public int rates_count;
    public String address;
    public int reviews_count;
    public float rate_average;
    public Object delivery;
    public String phone_number;
    public Object phone_number_decorated;
    public String url;
    public Object branding_feature;
    public Coordinate coordinates;
    public boolean is_theater;
    public List<String> movies;
    public boolean premium;
    public Object branding;
    public Object brand; // obtener email si esta registrado en el LinkedTreeMap del response
    public String wp_author_id;
    public String has_pay;
    public String schedule;
    public Object review; // obtener objetos o lista de comentarios
    public String schedule_search;
    public String schedule_simple;
    public boolean is_open;
    public String distance;
    public String start_date;
    public String end_date;
    public String dateInterval;
    public String price;
}
public class StackApiResponse {
    public String from;
    public Copie copies;
    public Boolean error_location;
    public String searchTerm;
    public String rate;
    public int radio;
    public List<String> searchedEntities;
    public Boolean isOpen;
    public int total_entries;
    public int total_pages;
    public int current_page;
    public List<Filter> filters;
    public List<String> did_you_mean;
    public List<Facet> facets;
    public List<Feature> features;
    public int featuresCount;
    public Object neighborhoods;
    public Object genres;
    public List<Result> results;
}
