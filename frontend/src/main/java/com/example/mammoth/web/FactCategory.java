package com.example.mammoth.web;

import java.util.List;

public class FactCategory {

    private String name;
    private List<MammothFact> facts;

    public FactCategory() {
    }

    public FactCategory(String name, List<MammothFact> facts) {
        this.name = name;
        this.facts = facts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MammothFact> getFacts() {
        return facts;
    }

    public void setFacts(List<MammothFact> facts) {
        this.facts = facts;
    }
}
