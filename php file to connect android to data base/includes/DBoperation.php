<?php

class operations{

	private $con;

	function __construct()
	{
		require_once dirname(__FILE__).'/connection.php';
		$dbc = new DBconnect();
		$this->con = $dbc->connect();
	}


	function insert( $Email , $Password , $Previlage )
	{
		$query = $this->con->prepare("INSERT INTO authentication VALUES(?,?,?);"); //preparing query
		$query->bind_param("sss" , $Email , $Password , $Previlage); 
		//binding parmeters (first parameters three s's each one express string the data type of each attribute
	
		if($query->execute())
		{
			return true;
		} 
		else
		{
			return false;
		}
	}

	function CheckEmail($Email)
	{
		$sql = "SELECT * FROM authentication where Email = '$Email' ;";
		$result = mysqli_query($this->con , $sql);
		$resultcheck = mysqli_num_rows($result);
		if($resultcheck >0)
		{
			$row = mysqli_fetch_assoc($result);
			return $row['Password'] ;
		}
		else
		{
			return 'F';
		}

	}

	Function GetTypeOfUser($Email)
	{
		$sql = "SELECT Previlage FROM authentication where Email = '$Email' ;";
		$result = mysqli_query($this->con , $sql);
		$resultcheck = mysqli_num_rows($result);
		if($resultcheck >0)
		{
			$row = mysqli_fetch_assoc($result);
			return $row['Previlage'] ;
		}
		else
		{
			return 'F';
		}

	}

	function GetName($type , $Email)
	{

		$query ; 
		

		if($type == 'S' OR $type == 'R' )
		{
			$query = "SELECT Fname , Lname FROM student where Email = '$Email'  ;";
		}
		else
		{
			$query = "SELECT Fname , Lname FROM teacher where Email = '$Email'  ;";
		}



		$result = mysqli_query($this->con , $query);
		$resultcheck = mysqli_num_rows($result);
		$row = mysqli_fetch_assoc($result);

		return $row;

	}

	function CheckTeacherExistance($Fname , $Lname ,$GrYear)
	{
		$query = "SELECT * FROM teacher where Fname = '$Fname' and Lname = '$Lname' and Gyear = $GrYear ;";
		$result = mysqli_query($this->con , $query);
		$resultcheck = mysqli_num_rows($result);
		if($resultcheck > 0 )
			return true;
		else
			return false;

	}

	function CheckStudentExistance($Id)
	{
		$query = "SELECT * FROM student where Id = $Id ;";
		$result = mysqli_query($this->con , $query);
		$resultcheck = mysqli_num_rows($result);
		if($resultcheck > 0 )
			return true;
		else
			return false;



	}

	function TeacherSignUP($Fname , $Lname , $Email , $Password , $type , $GrYear)
	{

		$result = $this->CheckTeacherExistance($Fname ,$Lname , $GrYear);
		if($result)
			return 0;
			

		$result = $this->CheckEmail($Email);

		if($result != 'F')
			return 1;

		$sql = "Insert Into authentication values(? , ? , ?);";

		$query = $this->con->prepare($sql); //preparing query
		$query->bind_param("sss" , $Email , $Password , $type);

		//binding parmeters (first parameters three s's each one express string the data type of each attribute
	
		if($query->execute())
		{
			
			$sql = "INSERT INTO teacher values(? , ? , ? , ?)";
			$query = $this->con->prepare($sql); //preparing query
			$query->bind_param("ssis" , $Fname , $Lname , $GrYear , $Email);
			if($query->execute()) 
			{
				return 3;
			}
			else
			{
				$sql = "DELETE FROM authentication where Email = $Email";
				$result = mysqli_query($this->con , $query);

				return 4;
			}

		} 
		else
		{
			return 2;
		}

	}



	function StudentSignUp($Fname , $Lname , $Email , $Password , $YearNum , $Sec , $Bn)
	{
		$Id = $YearNum *1000 + $Sec * 100 + $Bn ;

		$result = $this->CheckStudentExistance($Id);

		if($result)
			return 0;

		$result = $this->CheckEmail($Email);

		if($result != 'F')
			return 1;

		$sql = "Insert Into authentication values(? , ? , ?);";

		$query = $this->con->prepare($sql); //preparing query
		$type = 'S' ;
		$query->bind_param("sss" , $Email , $Password , $type);

		//binding parmeters (first parameters three s's each one express string the data type of each attribute
	
		if($query->execute())
		{
			$sql = "INSERT INTO student values(? , ? , ? , ? , ? , ? , ?)";
			$query = $this->con->prepare($sql); //preparing query
			$query->bind_param("iiisssi" , $Id , $Sec , $Bn , $Fname , $Lname , $Email , $YearNum);
			if($query->execute()) 
			{
				

				return 3;
			}
			else
			{
				$sql = "DELETE FROM authentication where Email = '$Email';";
				$result = mysqli_query($this->con , $sql);

				return 4;
			}

		}
		else
		{
			return 2;
		}



	}

	function RepresentativePost($Email , $Color , $text , $date,$YearNum )
	{
		$query = "SELECT Id FROM student where Email ='$Email' ";
		$result = mysqli_query($this->con , $query);
		$resultcheck = mysqli_num_rows($result);
		if($resultcheck <= 0 )
			return false ;
		$row = mysqli_fetch_assoc($result);
		$Id = $row['Id'];
		$sql = "INSERT into Post(Color , Text , Date , RepresentativeId , Syear ) VALUES(? , ? , ? , ? , ?) "; //the year, why needed in post?!
		$query = $this->con->prepare($sql); //preparing query
		$query->bind_param("sssii" , $Color , $text , $date , $Id ,$YearNum);
		if($query->execute()) 
		{
			return true  ;
		}
		else
		{
			return false ;
		}



	}

	function TeacherPost($Email, $Color , $text , $date ,$YearNum)
	{
		$query = "SELECT Fname , Lname , Gyear FROM teacher where Email ='$Email' ";
		$result = mysqli_query($this->con , $query);
		$resultcheck = mysqli_num_rows($result);
		if($resultcheck <= 0 )
			return false ;
		$row = mysqli_fetch_assoc($result);
		$Fname = $row['Fname'];
		$Lname = $row['Lname'];
		$GrYear = $row['Gyear'];
		$sql = "INSERT into Post(Color , Text , Date ,Fname , Lname , Gyear, Syear ) VALUES(? , ? , ? , ? ,? , ? , ?) ";
		$query = $this->con->prepare($sql); //preparing query
		$query->bind_param("sssssii" , $Color , $text , $date , $Fname , $Lname , $GrYear , $YearNum);
		if($query->execute()) 
		{
			return true  ;
		}
		else
		{
			return false ;
		}



	}

	function UpdatePost($text , $id)
	{
		$sql = "update post set Text = '$text' where Id = $id ;";
		$result = mysqli_query($this->con , $sql);

		return $result; 

	}

	function AllPosts($YearNum)
	{
		$query = "SELECT * from post where Syear = $YearNum ORDER BY Date ;";
		$result = mysqli_query($this->con , $query);
		return $result ;
	}

	function GetTeacher($Email)
	{
		$query = "SELECT * FROM teacher where Email = '$Email' ;"; 
		$result = mysqli_query($this->con , $query);
		$row = mysqli_fetch_assoc($result);
		return $row;
	}

	function YearsTecherIn($Fname , $Lname , $Gyear)
	{
		$query = "SELECT DISTINCT YearNum from course where CourseId in (select CourseId from teach where Fname = '$Fname' and 
		Lname = '$Lname' and Gyear = '$Gyear'); ";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

	function AllDoctors()
	{
		$query = "SELECT * FROM teacher where Email in (SELECT Email From authentication where Previlage = 'D')  ;"; 
		$result = mysqli_query($this->con , $query);
		return $result ;

	}


	function AllTeacherAssistants()
	{
		$query = "SELECT * FROM teacher where Email in (SELECT Email From authentication where Previlage = 'T')  ;"; 
		$result = mysqli_query($this->con , $query);
		return $result ;

	}  

	function AllTeachers()
	{
		$query = "SELECT * FROM teacher where Email in (SELECT Email From authentication where Previlage = 'T')  ;"; 
		$result = mysqli_query($this->con , $query);
		return $result ;

	}  

	function AllPostsOfATeacher($Email ,$YearNum )
	{
		
		$row = $this->GetTeacher($Email);
		
		$Fname = $row['Fname'];
		$Lname = $row['Lname'];
		$Gyear = $row['Gyear'];
		$query = "SELECT * From post where Fname = '$Fname' AND Lname = '$Lname' AND Gyear = $Gyear AND Syear = $YearNum ORDER BY Date ;";
		$result2 = mysqli_query($this->con , $query);
		return $result2;

	}

	function AddCourse($CourseId , $CourseName , $YearNum)
	{
		$sql = "INSERT INTO course values(? , ? , ?) ; ";
		$query = $this->con->prepare($sql);
		$query->bind_param("ssi" , $CourseId , $CourseName , $YearNum);

		if($query->execute()) 
		{
			return true  ;
		}
		else
		{
			return false ;
		}


	}

	function DeletePost($Id)
	{
		$query = "DELETE from post where Id = $Id";
		$result = mysqli_query($this->con , $query);
		return $result;

	}


	function GetStudent($Id)
	{

		$query = "SELECT * From student where Id = $Id ;";
		$result = mysqli_query($this->con , $query);
		return $result ; 

	}

	function GetStudent2($Email)
	{
		$query = "SELECT * From student where Email = '$Email' ;";
		$result = mysqli_query($this->con , $query);
		return $result ; 
	}

	function TeachIn($Fname , $Lname , $Gyear , $CourseId  )
	{
		$sql = "INSERT into teach values(?,?,?,?) ; ";
		$query = $this->con->prepare($sql);
		$query->bind_param("ssis" , $Fname , $Lname , $Gyear , $CourseId);

		if($query->execute()) 
		{
			return true  ;
		}
		else
		{
			return false ;
		}

	}

	function Representative($YearNum)
	{
		$query = "SELECT RepresentativeId from styear where YearNumber = $YearNum ;";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

	function AllCoursesInYear($YearNum)
	{
		$query = "SELECT * from course where YearNum = $YearNum ; ";
		$result = mysqli_query($this->con , $query);
		return $result;

	}
	function StudentContact($YearNum)
	{
		$query = "SELECT * from student where YearId = $YearNum ;" ;
		$result = mysqli_query($this->con , $query);
		return $result;

	}

	function TeacherContact()
	{
		$query = "SELECT * from teacher ;" ;
		$result = mysqli_query($this->con , $query);
		return $result;
	}


	function AddAssignment( $CourseId , $Link , $Deadline,$SubmitEmail)
	{
		$sql = "INSERT into assignment(CourseId , Link , Deadline,SubmitEmail) values(?,?,?,?) ; ";
		$query = $this->con->prepare($sql);
		$query->bind_param("ssss"  , $CourseId , $Link , $Deadline,$SubmitEmail);

		if($query->execute()) 
		{
			return true  ;
		}
		else
		{
			return false ;
		}
	}

	function UpdateAssignment($AssNum , $CourseId , $Link , $Deadline , $SubmitEmail)
	{
		$query = "UPDATE assignment set Link = '$Link' , Deadline = '$Deadline' ,SubmitEmail = '$SubmitEmail' where AssNum = $AssNum AND CourseId = '$CourseId' ; ";
		$result = mysqli_query($this->con , $query);
		return $result;
	}


	function AllAssignmentsInCourse($CourseId)
	{

		$query = "SELECT * from assignment where CourseId = '$CourseId' ;";
		$result = mysqli_query($this->con , $query);
		return $result;


	}

	function AddOfficeHours($Day , $Start , $Until , $Fname, $Lname , $Gyear)
	{

		$sql = "INSERT into office_hours values(?,?,?,?,?,?) ; ";
		$query = $this->con->prepare($sql);
		$query->bind_param("sssssi" , $Day, $Start ,$Until , $Fname , $Lname , $Gyear);

		if($query->execute()) 
		{
			return true  ;
		}
		else
		{
			return false ;
		}
	}

	function GetAllTeachersInCourse($CourseId)
	{
		$query = "SELECT * from teach where CourseId = '$CourseId' ;";
		$result = mysqli_query($this->con , $query);
		return $result;

	}

	function GetOfficeHours($Fname , $Lname , $Gyear)
	{
		$query = "SELECT Day , Start , Until from office_hours where Fname = '$Fname' AND Lname = '$Lname' AND $Gyear = '$Gyear' ; ";
		$result = mysqli_query($this->con , $query);
		return $result ;

	}

	function AddMaterial($Topic , $CourseId , $Link , $Type)
	{
		$sql = "INSERT INTO material values(?,?);";
		$query = $this->con->prepare($sql);
		$query->bind_param("ss" , $Topic , $CourseId);

		$result = $query->execute();

		$sql = "INSERT INTO content values(?,?,?,?);";
		$query = $this->con->prepare($sql);
		$query->bind_param("ssss" ,$Link , $Type , $Topic , $CourseId);

		if($query->execute())
			return true;
		else
			return false;

	}

	function GetMaterialsInCourse($CourseId)
	{
		$query = "SELECT * from material where CourseId = '$CourseId'; ";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

	function ContentsOfMaterial($CourseId)
	{
		$query = "SELECT * from content where CourseId = '$CourseId' ;" ;
		$result = mysqli_query($this->con , $query);
		return $result;

	}

	function AddProject($Title , $Link , $CourseId , $MaxNum , $SubmitEmail)
	{

		$sql = "INSERT INTO project values(?,?,?,?,?) ; " ;
		$query = $this->con->prepare($sql);
		$query->bind_param("sssis" , $Title , $CourseId , $Link , $MaxNum, $SubmitEmail);
		if($query->execute())
		{
			return true;
		}
		else
		{
			return false ; 
		}


	}

	function UpdateProject($Title , $Link , $CourseId , $MaxNum , $SubmitEmail)
	{
		$query = "UPDATE project set Link = '$Link' , MaxNum = '$MaxNum' , SubmitEmail = '$SubmitEmail' where CourseId = '$CourseId' and Title = '$Title';  ";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

	function DeleteCourse($CourseId)
	{
		$query = "DELETE from course where CourseId = '$CourseId';";
		$result = mysqli_query($this->con , $query);
		return $result;

	}

	function DeleteAssignment($CourseId , $AssNum)
	{
		$query = "DELETE from assignment where CourseId = '$CourseId' and AssNum = '$AssNum' ;";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

	function DeleteProject($CourseId , $Title)
	{
		$query = "DELETE from project where CourseId = '$CourseId' AND Title = '$Title' "  ;
		$result = mysqli_query($this->con , $query);
		return $result;

	}

	function DeleteOfficeHours($Day , $Start , $Until , $Fname , $Lname , $Gyear)
	{
		$query = "DELETE from office_hours where Day = '$Day' AND Start = '$Start' AND Until = '$Until' AND Fname = '$Fname' AND 
		Lname = '$Lname' AND Gyear =  $Gyear ;";
		$result = mysqli_query($this->con , $query);
		return $result;
	}
	
	function AllProjects($CourseId)
	{
		$query = "SELECT * From project where CourseId = '$CourseId' ;";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

	function AllAvailabeProjects($CourseId,$StudentId)
	{
		$query = "SELECT * From project where CourseId = '$CourseId' AND Title not IN (SELECT Title From inteam where StudentId ='$StudentId') ;";
		$result = mysqli_query($this->con , $query);
		return $result;
	}


	function UpdateCourse($CourseId , $CourseName )
	{

		$query = "UPDATE course set CourseName = '$CourseName' where CourseId = '$CourseId'; ";
		$result = mysqli_query($this->con , $query);
		return $result ;

	}

	function SubmitAssignment($AssNum , $CourseId , $StudentId)
	{
		$sql = "INSERT INTO asssubmit values(?,?,?);";
		$query = $this->con->prepare($sql);
		$query->bind_param("iis" , $StudentId , $AssNum , $CourseId);
		if($query->execute())
		{
			return true;
		}
		else
		{
			return false ; 
		}



	}

	function AllStudentsNotInTeamInProj($Title , $CourseId ,$YearId)
	{

		$query = "SELECT * from student as S where YearId='$YearId' and not exists(select * from inteam as T where 
		T.StudentId = S.Id and T.Title = '$Title' and T.CourseId = '$CourseId' );  ";
		$result = mysqli_query($this->con , $query);
		return $result;
		

	}

	function CreateNewTeamInProject($TeamNumber , $Title , $CourseId )
	{
		//actual number of members = 0 in the beginning
		$n = 0;
		$sql = "INSERT INTO team values(?,?,?,?);";
		$query = $this->con->prepare($sql);
		$query->bind_param("issi" , $TeamNumber , $Title , $CourseId , $n);
		if($query->execute())
		{
			return true;
		}
		else
		{
			return false ; 
		}


	}

	function DeleteMaterial($CourseId , $Topic)
	{
		$query = "DELETE from material where CourseID = '$CourseId' and Topic = '$Topic' ; ";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

	function DeleteMaterialContent($CourseId , $Topic , $Type , $Link)
	{
		$query = "DELETE from content where CourseID = '$CourseId' and Topic = '$Topic' and Type = '$Type' and Link = '$Link' ; ";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

	function MaxNum($Title , $CourseId)
	{
		$query = "SELECT MaxNum from project where CourseId = '$CourseId' and Title = '$Title' ; ";
		$result = mysqli_query($this->con , $query);
		$row = mysqli_fetch_assoc($result);
		$MaxNumber = $row['MaxNum'];
		return $MaxNumber;

	}

	function ActualNumber($CourseId , $Title , $TeamNumber)
	{
		$query2 = "SELECT ActualNum from team where CourseId = '$CourseId' and Title = '$Title' and TeamNumber = $TeamNumber  ; ";
		$result2 = mysqli_query($this->con , $query2);
		$row2 = mysqli_fetch_assoc($result2);
		$ActNum = $row2['ActualNum'];
		return $ActNum;
	}

	function TeamInfo($CourseId , $Title , $TeamNumber)
	{
		$query2 = "SELECT * from inteam where CourseId = '$CourseId' and Title = '$Title' and TeamNumber = $TeamNumber  ; ";
		$result2 = mysqli_query($this->con , $query2);
		$row2 = mysqli_fetch_assoc($result2);
		$ActNum = $row2['ActualNum'];
		return $ActNum;
	}

	function AddMemberInTeam($StudentId , $TeamNumber , $CourseId , $Title)
	{
		//get max number of members allowed for this project team

		$MaxNumber = $this->MaxNum($Title , $CourseId);
		
		//get the actual number of members in the team

		$ActNum = $this->ActualNumber($CourseId , $Title , $TeamNumber);

		if($ActNum < $MaxNumber)
		{
			$sql = "INSERT INTO inteam values(?,?,?,?);";
			$query = $this->con->prepare( $sql );
			$query->bind_param("iiss" , $StudentId , $TeamNumber , $Title , $CourseId);
			if( $query->execute() )
			{
				$ActNum =$ActNum+1; 
				$query = "UPDATE team set ActualNum = $ActNum where CourseId = '$CourseId' and Title = '$Title' and
				 TeamNumber = $TeamNumber  ; ";
				 $result = mysqli_query($this->con , $query);

				return 1;
			}
			else
			{
				return 2 ; 
			}

		}
		else
		{

			return 0;

		}

	}



	function DeleteTeamMember($StudentId , $TeamNumber , $CourseId , $Title)
	{
		$query = "DELETE from inteam where StudentId = $StudentId and TeamNumber = $TeamNumber and CourseId = '$CourseId' and 
		Title = '$Title' ;";
		$result = mysqli_query($this->con , $query);
		if($result)
		{
			$ActNum = $this->ActualNumber($CourseId , $Title , $TeamNumber);
			$ActNum--;
			$query = "UPDATE team set ActualNum = $ActNum where CourseId = '$CourseId' and Title = '$Title' and
				 TeamNumber = $TeamNumber  ; ";
			$result2 = mysqli_query($this->con , $query);
			return true;

		}
		else
		{
			return false;
		}

	}

	function DeleteTeam($TeamNumber , $CourseId , $Title)
	{
		$query = "DELETE from team where TeamNumber = '$TeamNumber' and CourseId = '$CourseId' and Title = '$Title';  ";
		$result = mysqli_query($this->con , $query);
		return $result;
	}


	function TeamsInProj($CourseId , $Title)
	{
		$query = "SELECT TeamNumber from team where CourseId = '$CourseId' and Title = '$Title'; ";
		$result = mysqli_query($this->con , $query);
		return $result;

	} 

	function StudentsInTeam($Title , $CourseId , $TeamNumber)
	{
		$query = "SELECT * from student where Id in (SELECT StudentId from inteam where Title = '$Title' and CourseId = '$CourseId' and TeamNumber = $TeamNumber);";
		$result = mysqli_query($this->con , $query);
		return $result;

	}
	function Years()
	{
		$query = "SELECT * from styear ";
		$result = mysqli_query($this->con , $query);
		return $result;
	}

//statictics of app

function NumberOfStudentsInYear($YearNum)
{
	$query  = "SELECT Count(*) as CountStudents from student where YearId = $YearNum;";
	$result = mysqli_query($this->con , $query);
	$r = mysqli_fetch_object($result);
	return $r;

}

function NumberOfCoursesInYear($YearNum)
{
	$query  = "SELECT Count(*) as CountCourses from course where YearNum = $YearNum;";
	$result = mysqli_query($this->con , $query);
	$r = mysqli_fetch_object($result);
	return $r;
}

function NumberOfProjectsInYear($YearNum)
{
	$query  = "SELECT Count(*) as CountProjects from project where CourseId in (SELECT CourseId from course  where YearNum = $YearNum);";
	$result = mysqli_query($this->con , $query);
	$r = mysqli_fetch_object($result);
	return $r;
}

function NumberOfDoctors($YearNum)
{
	$query  = "SELECT Count(*) as CountDoctors from teacher as t where t.Email in (select Email from authentication where Previlage = 'D') and exists(Select * from teach as c where t.Fname = c.Fname and 
	t.Lname = c.Lname and t.Gyear = c.Gyear and c.CourseId in(select CourseId from course where YearNum = $YearNum)   );";
	$result = mysqli_query($this->con , $query);
	$r = mysqli_fetch_object($result);
	return $r;
}

function NumberOfTeacherAssistants($YearNum)
{
	$query  = "SELECT Count(*) as CountAssistants from teacher as t where t.Email in (select Email from authentication where Previlage = 'T') and exists(Select * from teach as c where t.Fname = c.Fname and 
	t.Lname = c.Lname and t.Gyear = c.Gyear and c.CourseId in(select CourseId from course where YearNum = $YearNum)   );";
	$result = mysqli_query($this->con , $query);
	$r = mysqli_fetch_object($result);
	return $r;
}

function NumberOfAssignmentsInCoursesInYear($YearNum)
{
	$query = "SELECT Count(*) as CountAss , c.CourseName from assignment as a , course as c where a.CourseId = c.CourseId and c.YearNum = $YearNum group by c.CourseName ;";
	$result = mysqli_query($this->con , $query);
	return $result ;
}

function NumberOfTeamsOfProjectsInYear($YearNum)
{

	$query = "SELECT Count(*) as CountTeams , t.Title ,t.CourseId from Team as t where t.CourseId in (
			select CourseId from course where YearNum = $YearNum ) group by t.title , t.CourseId ; ";
	$result =mysqli_query($this->con , $query);
	return $result;


}

function NumberOfTeachersInCourse($YearNum)
{
	$query = "SELECT Count(*) as CountDoc , c.CourseName from teach as t , course as c where t.CourseId = c.CourseId and c.YearNum = $YearNum group by c.CourseName ; ";
	$result =mysqli_query($this->con , $query);
	return $result;

}

function GetCourseOfTeacherInYear($Fname , $Lname , $Gyear , $YearNum)
	{
		$query = "SELECT * from course where CourseId in (SELECT CourseId from teach where Fname = '$Fname' and Lname = '$Lname'
		and Gyear = $Gyear) and YearNum = $YearNum ;";
		$result = mysqli_query($this->con , $query);
		return $result;

	}






}