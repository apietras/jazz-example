package testrest;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testcase", namespace = "http://jazz.net/xmlns/alm/qm/v0.1/")
public class TestCaseXmlModel {

    private String title;
    private String description;
    private Date updated;

    /*
     * Do tej listy wpadną wszystkie niezmapowane pola, dzięki czemu nie trzeba
     * mapować niepotrzebnych i absolutnie wszystkich
     */
    private List<Object> anything;

    @XmlElement(name = "title", namespace = "http://purl.org/dc/elements/1.1/")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "description", namespace = "http://purl.org/dc/elements/1.1/")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "updated", namespace = "http://jazz.net/xmlns/alm/v0.1/")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @XmlAnyElement(lax = true)
    public List<Object> getAnything() {
        return anything;
    }

    public void setAnything(List<Object> anything) {
        this.anything = anything;
    }

    @Override
    public String toString() {
        return "TestCaseXmlModel [title=" + title + ", description=" + description + ", updated=" + updated + "]";
    }

}
