package ng.startup.journeyapp.fragments.goals;

public class GoalsClass {

    public int goal_id;
    public String goal_title;
    public String goal_description;
    public String goal_color;
    public String goal_due_date;
    public String goal_due_date_format;
    public String goal_date_created;
    public String goal_date_created_format;
    public String goal_days_left;
    public String goal_no_steps;
    public String goal_status;


    public GoalsClass(int goal_id, String goal_title, String goal_description, String goal_color,
                      String goal_due_date, String goal_due_date_format, String goal_date_created,
                      String goal_date_created_format, String goal_days_left, String goal_no_steps,
                      String goal_status){

        this.goal_id = goal_id;
        this.goal_title = goal_title;
        this.goal_description = goal_description;
        this.goal_color = goal_color;
        this.goal_due_date = goal_due_date;
        this.goal_due_date_format = goal_due_date_format;
        this.goal_date_created = goal_date_created;
        this.goal_date_created_format = goal_date_created_format;
        this.goal_days_left = goal_days_left;
        this.goal_no_steps = goal_no_steps;
        this.goal_status = goal_status;

    }

    public GoalsClass(int goal_id, String goal_title, String goal_description, String goal_color,
                      String goal_due_date, String goal_due_date_format, String goal_date_created,
                      String goal_date_created_format, String goal_status){

        this.goal_id = goal_id;
        this.goal_title = goal_title;
        this.goal_description = goal_description;
        this.goal_color = goal_color;
        this.goal_due_date = goal_due_date;
        this.goal_due_date_format = goal_due_date_format;
        this.goal_date_created = goal_date_created;
        this.goal_date_created_format = goal_date_created_format;
        this.goal_days_left = goal_days_left;
        this.goal_no_steps = goal_no_steps;
        this.goal_status = goal_status;

    }

    public int getGoalId() {
        return goal_id;
    }
    public void setGoalId(int goal_id) {
        this.goal_id =goal_id;
    }

    public String getGoalTitle() {
        return goal_title;
    }
    public void setGoalTitle(String goal_title) {
        this.goal_title = goal_title;
    }

    public String getGoalDescription() {
        return goal_description;
    }
    public void setGoalDescription(String goal_description) {
        this.goal_description = goal_description;
    }

    public String getGoalColor() {
        return goal_color;
    }
    public void setGoalColor(String goal_color) {
        this.goal_color = goal_color;
    }

    public String getGoalDueDate() {
        return goal_due_date;
    }
    public void setGoalDueDate(String goal_due_date) {
        this.goal_due_date = goal_due_date;
    }

    public String getGoalDueDateFormat() {
        return goal_due_date_format;
    }
    public void setGoalDueDateFormat(String goal_due_date_format) {
        this.goal_due_date_format = goal_due_date_format;
    }

    public String getGoalDateCreated() {
        return goal_date_created;
    }
    public void setGoalDateCreated(String goal_date_created) {
        this.goal_date_created = goal_date_created;
    }

    public String getGoalDateCreatedFormat() {
        return goal_date_created_format;
    }
    public void setGoalDateCreatedFormat(String goal_date_created_format) {
        this.goal_date_created_format = goal_date_created_format;
    }

    public String getGoalNoSteps() {
        return goal_no_steps;
    }
    public void setGoalNoSteps(String goal_no_steps) {
        this.goal_no_steps = goal_no_steps;
    }

    public String getGoalDaysLeft() {
        return goal_days_left;
    }
    public void setGoalDaysLeft(String goal_days_left) {
        this.goal_days_left = goal_days_left;
    }

    public String getGoalStatus() {
        return goal_status;
    }
    public void setGoalStatus(String goal_status) {
        this.goal_status = goal_status;
    }
}