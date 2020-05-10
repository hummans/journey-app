package ng.startup.journeyapp.fragments.today;

public class StepsClass {

    public int step_id;
    public String step_title;
    public String step_date_created;
    public String step_date_created_format;
    public String goal_color;
    public String goal_title;
    public String step_journal;
    public String step_status;

    public StepsClass(int step_id, String step_title, String step_date_created, String step_date_created_format,
                      String goal_color, String goal_title, String step_journal, String step_status){

        this.step_id = step_id;
        this.step_title = step_title;
        this.step_date_created = step_date_created;
        this.step_date_created_format = step_date_created_format;
        this.goal_color = goal_color;
        this.goal_title = goal_title;
        this.step_journal = step_journal;
        this.step_status = step_status;
    }

    public int getStepId() {
        return step_id;
    }
    public void setStepId(int step_id) {
        this.step_id = step_id;
    }

    public String getStepTitle() {
        return step_title;
    }
    public void setStepTitle(String step_title) {
        this.step_title = step_title;
    }

    public String getStepDateCreated() {
        return step_date_created;
    }
    public void setStepDateCreated(String step_date_created) {
        this.step_date_created = step_date_created;
    }

    public String getStepDateCreatedFormat() {
        return step_date_created_format;
    }
    public void setStepDateCreatedFormat(String step_date_created_format) {
        this.step_date_created_format = step_date_created_format;
    }

    public String getGoalColor() {
        return goal_color;
    }
    public void setGoalColor(String goal_color) {
        this.goal_color = goal_color;
    }

    public String getGoalTitle() {
        return goal_title;
    }
    public void setGoalTitle(String goal_title) {
        this.goal_title =goal_title;
    }

    public String getStepJournal() {
        return step_journal;
    }
    public void setJournalEntry(String step_journal) {
        this.step_journal = step_journal;
    }

    public String getStepStatus() {
        return step_status;
    }
    public void setUsername(String step_status) {
        this.step_status = step_status;
    }




}
