public class Job
{
  int jobId;
  String jobTitle;
  String industry;
  String description;
  int companyId;
  int managerId;
  String type; //@TODO change to char
  int numOpenSpots;
  int numApplicants;

  public Job()
  {

  }
  // 
  // public Job(String jobTitle, String industry, String description, int companyId, int managerId, String type)
  // {
  //   this.jobTitle = jobTitle;
  //   this.industry = industry;
  //   this.description = description;
  //   this.companyId = companyId;
  //   this.managerId = managerId;
  //   this.type = type;
  // }

  public void jobCreated()
  {
    System.out.println("Job created.");
    System.out.println("Job title: " + jobTitle);
    System.out.println("Industry: " + industry);
    System.out.println("CID: " + companyId + " MID: " + managerId);
    System.out.println("Description: " + description);
  }
}
