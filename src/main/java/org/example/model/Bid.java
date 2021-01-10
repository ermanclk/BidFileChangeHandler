package org.example.model;


import java.io.Serializable;
import java.util.Base64;

public class Bid implements Serializable {

    private String id;
    private String ty;
    private String ts;
    private String pl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTy() {
        return ty;
    }

    public void setTy(String ty) {
        this.ty = ty;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id='" + id + '\'' +
                ", ty='" + ty + '\'' +
                ", ts='" + ts + '\'' +
                ", pl='" +new String(Base64.getDecoder().decode(pl))+ '\'' +
                '}';
    }
}
