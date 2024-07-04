package com.ufund.api.ufundapi.model;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.api.ufundapi.persistence.UFundFileDAO;

public class Bundle {
    

    @JsonProperty("id") private int id; // id number
    @JsonProperty("name") private String name; //Bundle title
    @JsonProperty("needs") private List<Need> needs; //List of Bundled Needs
    


    /**
     * Bundle constructor, takes name and need list
     * @param needs List<Need> of needs in the Bundle
     * @param name String the title of the bundle
     */
    public Bundle (@JsonProperty("id") int id, @JsonProperty("name") String name,@JsonProperty("needs") List<Need> needs)
    {
        this.needs = needs;
        this.name = name;
        this.id = id;
    }

    /**
     * Gets the current list of needs in the Bundle
     * @return this.needs (List<Need>)
     */
    public List<Need> getList()
    {
        return this.needs;
    }

    /**
     * Sets the list of needs to a new list
     * @param newNeeds List<Need> an updated list of needs
     */
    public void setList(List<Need> newNeeds)
    {
        this.needs = newNeeds;
    }

    /**
     * access the name of the bundle
     * @return this.name the title of the Bundle
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Change the bundle's name
     * @param newName
     */
    public void setName(String newName)
    {
        this.name = newName;
    }

    public int getId()
    {
        return this.id;
    }

    /**
     * updates the list of needs based on changes to the DAO
     * removes deleted needs from the list, updates the contents of all remaining needs
     * @param dao the FileDAO for Needs
     */
    public void sync(UFundFileDAO dao)
    {
        for(Need n: this.needs)
        {
            Need temp = dao.getNeed(n.getId());
            if( temp==null)
            {
                this.needs.remove(n);
            }
            else
            {
                n = temp;
            }
            
        }
    }


    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Bundle)
        {
            Bundle o = (Bundle) other;
            if(o.getId()==this.id&&o.getName().equals(this.name)&&o.getList().size()==needs.size())
            {
                boolean x = true;
                for(Need n: needs)
                {
                    if (!o.getList().contains(n))
                    {
                        x = false;
                    }
                }
                return x;
            }
        }
        return false;
    }

}
