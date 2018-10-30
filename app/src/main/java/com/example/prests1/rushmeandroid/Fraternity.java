package com.example.prests1.rushmeandroid;

public class Fraternity {

    private String name;
    private String chapter;
    private int memberCount;
    private String desc;

    public Fraternity(String fraternityName, String fraternityChapter, int fraternityMembers, String fraternityDecription) {
        this.name = fraternityName;
        this.chapter = fraternityChapter;
        this.memberCount = fraternityMembers;
        this.desc = fraternityDecription;
    }

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

    public void test() {
        System.out.println(this.getChapter() + " of " + this.getName() + " has " + this.getMemberCount() + " brothers.");
    }

}
