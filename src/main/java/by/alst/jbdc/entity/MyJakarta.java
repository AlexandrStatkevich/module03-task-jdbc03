package by.alst.jbdc.entity;

import java.util.List;
import java.util.Objects;

public class MyJakarta {
    private String version;
    private String description;
    private List<Technology> technologyList;

    public MyJakarta() {
    }

    public MyJakarta(String version, String description, List<Technology> technologyList) {
        this.version = version;
        this.description = description;
        this.technologyList = technologyList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Technology> getTechnologyList() {
        return technologyList;
    }

    public void setTechnologyList(List<Technology> technologyList) {
        this.technologyList = technologyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyJakarta myJakarta = (MyJakarta) o;
        return Objects.equals(version, myJakarta.version) && Objects.equals(description, myJakarta.description) && Objects.equals(technologyList, myJakarta.technologyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, description, technologyList);
    }

    @Override
    public String toString() {
        return "MyJakarta{" +
               "version='" + version + '\'' +
               ",\n description='" + description + '\'' +
               ",\n technologyList=" + technologyList +
               '}';
    }
}
