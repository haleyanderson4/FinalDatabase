public class Job
{
  int jobId;
  String jobTitle;
  String industry;
  String description;
  int companyId;
  int managerId;
  char type;

  public Job()
  {

  }

  public Job(int jobId, String jobTitle, String industry, String description, int companyId, int managerId, char type)
  {
    this.jobId = jobId;
    this.jobTitle = jobTitle;
    this.industry = industry;
    this.description = description;
    this.companyId = companyId;
    this.managerId = managerId;
    this.type = type;
  }

  public void jobCreated()
  {
    System.out.println("Job created.");
    System.out.println("Job title: " + jobTitle);
    System.out.println("Industry: " + industry);
    System.out.println("Description: " + description);
  }
}
