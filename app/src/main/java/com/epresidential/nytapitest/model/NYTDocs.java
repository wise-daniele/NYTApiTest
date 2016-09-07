package com.epresidential.nytapitest.model;

/**
 * Created by daniele on 23/08/16.
 */
public class NYTDocs extends JsonObject{

    private Article[] docs;

    public Article[] getDocs(){
        return docs;
    }

}
