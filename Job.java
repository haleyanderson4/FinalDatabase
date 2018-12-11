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
  int stockOptions;
  float signingBonus;
  float salary;
  String payPeriod;
  float rate;
  String season;

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

}
