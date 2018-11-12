package com.example.prests1.rushmeandroid;

// This class determines the structure of the 'Fraternity' object.
public class Fraternity {

    //Private variables storing information for an individual fraternity.
    //To be extended... limited data being used at first for testing purposes.
    private String name;
    private String chapter;
    private int memberCount;
    private String desc;
    private String imgURL;
    private String calendarURL;

    //Constructor sets variables as these need only be determined on start/require no local update.
    public Fraternity(String fraternityName, String fraternityChapter, int fraternityMembers, String fraternityDecription) {
        this.name = fraternityName;
        this.chapter = fraternityChapter;
        this.memberCount = fraternityMembers;
        this.desc = fraternityDecription;
    }

    //Getters for info that is displayed.
    public String getName() {
        return this.name;
    }
    public String getChapter() {
        return this.chapter;
    }
    public int getMemberCount() {
        return this.memberCount;
    }
    public String getDescription() {
        return this.desc;
    }
    public String getImage() {
        return this.imgURL;
    }
    public String getCalendar() { return this.calendarURL; }

    //Temporary function established to verify info. (Functionality test)
    public void test() {
        System.out.println(this.getChapter() + " of " + this.getName() + " has " + this.getMemberCount() + " brothers.");
    }

}