package nyc.c4q.syd.updateme;

import java.util.List;

/**
 * Created by Yuliya Kaleda on 6/26/15.
 */
public class JobCard extends Card {
    private List<JobPosition> jobArray;

    public JobCard(List<JobPosition> jobArray) {
        this.jobArray = jobArray;
    }

    public List<JobPosition> getJobArray() {
        return jobArray;
    }

    public void setJobArray(List<JobPosition> jobArray) {
        this.jobArray = jobArray;
    }

    @Override
    public int getType() {
        return 1;
    }
}
