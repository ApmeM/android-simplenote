package org.apmem.widget.notes.datastore.model;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 20:35
 * To change this template use File | Settings | File Templates.
 */
public class ListElement {
    private String name = "Set name";
    private long id;
    private boolean edited = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }
}
