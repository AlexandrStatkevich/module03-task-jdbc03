package by.alst.jbdc.entity;

import java.util.Objects;

public class Technology {

    private String technologyName;
    private String technologyDescription;

    public Technology() {
    }

    public Technology(String technologyName, String technologyDescription) {
        this.technologyName = technologyName;
        this.technologyDescription = technologyDescription;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public String getTechnologyDescription() {
        return technologyDescription;
    }

    public void setTechnologyDescription(String technologyDescription) {
        this.technologyDescription = technologyDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technology that = (Technology) o;
        return Objects.equals(technologyName, that.technologyName) && Objects.equals(technologyDescription, that.technologyDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(technologyName, technologyDescription);
    }

    @Override
    public String toString() {
        return "Technology{" +
               "technologyName='" + technologyName + '\'' +
               ", technologyDescription='" + technologyDescription + '\'' +
               '}';
    }
}
