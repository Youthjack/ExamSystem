package com.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by takahiro on 2016/12/22.
 */
@Entity
public class Question {
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String ques;
    @Column
    private String ans;
    @Column
    private int point;
    @Column
    private String session;
    @Column
    private int type;
    @ManyToMany(mappedBy = "questionSet")
    private Set<Paper>papers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Set<Paper> getPapers() {
        return papers;
    }

    public void setPapers(Set<Paper> papers) {
        this.papers = papers;
    }
}
