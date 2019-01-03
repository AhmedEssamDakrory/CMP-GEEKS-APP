<?php

require_once '../includes/DBoperation.php';


switch($_GET['function']) {

case 'SignInCheck';
	SignInCheck();
	break;
case 'SignUpAsTeacher';
	SignUpAsTeacher();
	break;
case 'SignUpAsStudent';
	SignUpAsStudent();
	break;
case 'AddPost';
	AddPost();
	break;
case 'GetAllPosts';
	GetAllPosts();
	break;
case 'AddCourse';
	AddCourse();
	break;
case 'DeletePost';
	DeletePost();
	break;	
case 'UpdatePost';
	UpdatePost();
	break;	
case 'DoctorsInfo';
	DoctorsInfo();
	break;

case 'TeacherAssistantsInfo';
	TeacherAssistantsInfo();
	break;
case 'AssignTeacherToCourse';
	AssignTeacherToCourse();
	break;
case 'AllCoursesInYear';
	AllCoursesInYear();
	break;
case 'AllContactsInYearStudents';
	AllContactsInYearStudents();
	break;	
case 'AllContactsInYearTeachers';
	AllContactsInYearTeachers();
	break;
case 'AddAssignment';
	AddAssignment();
	break;
case 'Assignments';
	Assignments();
	break;
case 'AddOfficeHours';
	AddOfficeHours();
	break;

case 'GetOfficeHoursOfCourse';
	GetOfficeHoursOfCourse();
	break;
case 'AddMaterial';
	AddMaterial();
	break;
case 'Materials';
	Materials();
	break;
case 'AddProject';
	AddProject();
	break;

case 'GetProjects';
	GetProjects();
	break;
case 'UpdateCourse';
	UpdateCourse();
	break;


case 'UpdateAssignment';
	UpdateAssignment();
	break;
case 'DeleteOfficeHours';
	DeleteOfficeHours();
	break;
case 'DeleteProject';
	DeleteProject();
	break;
case 'SubmitAssignment';
	SubmitAssignment();
	break;
case 'CreateTeam';
	CreateTeam();
	break;
case 'AddMemberInTeam';
	AddMemberInTeam();
	break;
case 'AllStudentsNotInTeam';
	AllStudentsNotInTeam();
	break;
case 'DeleteTeamMember';
	DeleteTeamMember();
	break;
case 'RepresentativeIdOfYear';
	RepresentativeIdOfYear();
	break;
case 'TeamsInProj';
	TeamsInProj();
	break;
case 'DeleteTeam';
	DeleteTeam();
	break;
case 'YearsInfo';
	YearsInfo();
	break;
case 'DeleteCourse';
	DeleteCourse();
	break;
case 'DeleteAssignment';
	DeleteAssignment();
	break;
case 'DeleteMaterial';
	DeleteMaterial();
	break;
case 'DeleteContentOfMaterial';
	DeleteContentOfMaterial();
	break;
case 'ContentsOfMaterial';
	ContentsOfMaterial();
	break;
case 'Count_Statistics';
	Count_Statistics();
	break;
case 'Statistics_group';
	Statistics_group();
	break;
case 'UpdateProject';
	UpdateProject();
	break;
case 'StudentsInTeam';
	StudentsInTeam();
	break;
case 'TeacherInfoInCourse';
	TeacherInfoInCourse();
	break;
case 'GetAvailableProjects';
	GetAvailableProjects();
	break;
}



function SignInCheck()
{
	$response = array();
	$info = array();
		
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if( isset($_POST['Email']) AND isset($_POST['Password']) ) //check if all attribute vales is set........
		{

			$op = new operations();
			$result = $op->CheckEmail($_POST['Email']);
			if($result != 'F')
			{
				

				if($result == $_POST['Password'])
				{

					$response['LoggedIn'] = true;

					$type = $op->GetTypeOfUser($_POST['Email']);
					if($type == 'S' OR $type == 'R')
					{
						$student = $op->GetStudent2($_POST['Email']);
						$row = mysqli_fetch_assoc($student);
						$info['Id'] = $row['Id'];
						$info['Fname'] = $row['Fname'];
						$info['Lname'] = $row['Lname'];
						$info['Sec'] = $row['Sec'];
						$info['Bn'] = $row['Bn'];
						$info['YearNum'] = $row['YearId'];

					}
					else
					{
						$teacher = $op->GetTeacher($_POST['Email']);
						
						$info['Fname'] = $teacher['Fname'];
						$info['Lname'] = $teacher['Lname'];
						$info['Gyear'] = $teacher['Gyear'];
						$years = $op->YearsTecherIn($info['Fname'],$info['Lname'],$info['Gyear']);
						$info['years'] = array();
						$i = 0;
						while($row = mysqli_fetch_assoc($years))
						{
							$info['years'][$i] = $row['YearNum'];
							$i++;
						}

					}


					$response['Type'] = $type;

					$response['error'] = false;
					$response['message']="logged in as $type ";

					$name =array();
					$name = $op->GetName($type ,$_POST['Email']);

					$response['Fname'] = $name['Fname'];
					$response['Lname'] = $name['Lname'];


				}
				else
				{

					$response['ValidPassword'] = 0;

					$response['error'] = false;
					$response['message']=" invalid Password ";
				}
				

			}
			else
			{
				$response['ValidEmail'] = 1;
				$response['error'] = false;
				$response['message']=" invalid Email ";


			}


		}
		else
		{
			$response['error'] = true ;
			$response['message']="please insert your email and password to log in";

		}


	}
	else
	{
		$response['error'] = true ;
		$response['message']="inavlid request";
	}

	$MainJson = array('info' => $info , 'response' =>$response);
	echo json_encode($MainJson)."<br>";
	
}

function SignUpAsTeacher()
{
	$response =array(); 
	$response['SignedUp'] = false;
	
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['Fname'] ) AND isset($_POST['Lname'] ) AND isset($_POST['Email']) AND isset($_POST['Password'] ) AND isset($_POST['type'])  AND isset($_POST['Gyear']) )
		{
			$op = new operations();
			$result = $op->TeacherSignUP($_POST['Fname'],$_POST['Lname'],$_POST['Email'],$_POST['Password'] ,$_POST['type'] , $_POST['Gyear']);


			if($result == 0)
			{
			
				$response['error'] = true;
				$response['message'] = " already exists ";


			}
			else if($result == 1)
			{
				
				$response['error'] = true;
				$response['message'] = " email has been used before ";


			}
			else if($result == 2)
			{
				if(strlen($_POST['Password'] ) >8)
				{
					
					$response['error'] = true;
					$response['message'] = " invalid email ";

				}
				else
				{
			
					$response['error'] = true;
					$response['message'] = " too short password length must br greater than 8 characters ";
				}

			}
			else if($result == 3)
			{
				$response['SignedUp']=true;
				$response['error'] = false;
				$response['message'] = " hello to cmp_geeks World ^_^";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = "invlaid parameters";
			}

			

		}
		else
		{
			$response['error'] = true;
			$response['message'] = "missed fields ";

		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = "invalid request ";

	}

	echo json_encode($response)."<br>";
}


function SignUpAsStudent()
{
	$response = array();
	$response['SignedUp'] = false ;
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{

		if(isset($_POST['Fname']) AND isset($_POST['Lname']) AND isset($_POST['Email']) AND isset($_POST['Password']) AND isset($_POST['YearNum']) AND isset($_POST['Sec']) AND isset($_POST['Bn']) )
		{


			$op = new operations();
			$result = $op->StudentSignUP($_POST['Fname'] , $_POST['Lname'] , $_POST['Email'],$_POST['Password'] ,$_POST['YearNum'] , $_POST['Sec'] , $_POST['Bn']);


			if($result == 0)
			{
				$StudentExsists = 1;
				$response['error'] = true;
				$response['message'] = " already exists ";


			}
			else if($result == 1)
			{
				$EmailAlredyExists = 1;
				$response['error'] = true;
				$response['message'] = " email has been used before ";


			}
			else if($result == 2)
			{
				if(strlen($_POST['Password'] ) >8)
				{
					$invalidEmail = 1;
					$response['error'] = true;
					$response['message'] = " invalid email ";

				}
				else
				{
					$ShortPassowrd = 1;
					$response['error'] = true;
					$response['message'] = " too short password ,length must br greater than 8 characters ";
				}

			}
			else if($result == 3)
			{
				$response['SignedUp'] = true;
				$response['error'] = false;
				$response['message'] = " hello to cmp_geeks World ^_^";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = "invlaid parameters";
			}


		}
		else
		{
			$response['error']=true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error']=true;
		$response['message'] = "invalid request ";
	}

	echo json_encode($response)."<br>";

}

function AddPost()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		$op = new operations();
		$result = $op->GetTypeOfUser($_POST['Email']);
		$f=false;

		if($result == 'R')
		{
			$f = $op->RepresentativePost( $_POST['Email'],$_POST['Color'] , $_POST['Text'] , $_POST['Date'] , $_POST['YearNum'] );
		}
		else if($result == 'D' OR $result == 'T')
		{
			$f = $op->TeacherPost($_POST['Email'], $_POST['Color'] , $_POST['Text'] , $_POST['Date'] , $_POST['YearNum']  );

		}

		if($f)
		{
			$response['error']=false;
			$response['message'] = "Posted Succefully";

		}
		else
		{
			$response['error']=true;
			$response['message'] = "failed ya abdallah eb3tly valid parameters argoook! ";

		}
	}
	else
	{
		$response['error']=true;
		$response['message'] = "invalid request ";
	}

	echo json_encode($response)."<br>";

}



function UpdatePost()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if(isset($_POST['Text']) And isset($_POST['Id']))
		{
			$op = new operations();
			$result = $op->UpdatePost($_POST['Text'] , $_POST['Id']);
			if($result)
			{
				$response['error'] = false;
				$response['message'] = "updated Succefully";
			}
			else
			{
				$response['error'] = false;
				$response['message'] = "something wrong !";
			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = "missed fields";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = "invalid request";
	}
}

function GetAllPosts()
{
	
	
	$response = array();
	$Posts = array();
	 
	

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['Email']) AND isset($_POST['YearNum']) )
		{
			$op = new operations();

			$result = $op->GetTypeOfUser($_POST['Email']);

			if($result == 'S' OR $result == 'R')
			{
				$result = $op->AllPosts($_POST['YearNum']);

			}
			else if($result == 'D' OR $result == 'T')
			{
				$result = $op->AllPostsOfATeacher($_POST['Email'] ,$_POST['YearNum'] );

			}
			else
			{  
				$response['error'] = true;
				$response['message'] = "something wrong";
				return ;
			}

			
			$NumberOfPosts = mysqli_num_rows($result) ;
			$response['PostsNumber'] = $NumberOfPosts ;
			if($NumberOfPosts > 0)
			{
				$i = 0;
				while( $row = mysqli_fetch_assoc($result) )
				{
					if(is_null( $row['RepresentativeId'] ) )
					{
						$Posts[$i] = array('Text' => $row['Text'] , 'Id' => $row['Id'] , 'Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Date' => $row['Date'] );
					}
					else
					{
						$r = $op->GetStudent($row['RepresentativeId']);
						$name = mysqli_fetch_assoc($r);
						$Fname = $name['Fname'];
						$Lname = $name['Lname'];

						$Posts[$i] = array('Text' => $row['Text'] , 'Id' => $row['Id'] , 'Fname' => $Fname , 'Lname' => $Lname , 'Date' => $row['Date'] );

					}
					
					$i++;

				}

				$response['error'] = false;
				$response['message'] = "succeeded";



			} 
		}
		else
		{
			$response['error'] = true;
			$response['message'] = "missed parameters";

		}
		

	}
	else
	{
		$response['error'] = true;
		$response['message'] = "invalid request";
	}
	
	$habda = array('Posts' => $Posts,'response' => $response);
	
	echo json_encode($habda)."<br>";

}

function DeletePost()
{
	$response = array(); 
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['Id']))
		{
			$op = new operations();
			$result = $op->DeletePost($_POST['Id']);
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " Post Deleted ";
			}
			else
			{

				$response['error'] = true;
				$response['message'] = " something wrong happened ";
			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " Id of Post missed ";
		}

	}
	else
	{

		$response['error'] = true;
		$response['message'] = " wrong request ";
	}

	echo json_encode($response);

}

function AddCourse()
{
	
	$response = array(); 
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId']) AND isset($_POST['CourseName'])  AND isset($_POST['YearNum']))
		{
			$op = new operations();

			$result = $op->AddCourse($_POST['CourseId'] , $_POST['CourseName'],$_POST['YearNum']);

			if($result)
			{
				$response['error'] = false;
				$response['message'] = " added succefully ";


			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong maybe the course already exists ";

			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";

		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);



}

function DeleteAssignment()
{
	$response = array(); 
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId']) and isset($_POST['AssNum']) )
		{
			$op = new operations();

			$result = $op->DeleteAssignment($_POST['CourseId'] , $_POST['AssNum']);

			
			$response['error'] = false;
			$response['message'] = " Assignment Deleted ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";

		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);
}

function DeleteCourse()
{

	$response = array(); 
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId']) )
		{
			$op = new operations();

			$result = $op->DeleteCourse($_POST['CourseId']);

			
			$response['error'] = false;
			$response['message'] = " Course Deleted ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";

		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);

}

function UpdateCourse()
{

	$response = array(); 
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId']) AND isset($_POST['CourseName']) )
		{
			$op = new operations();

			$result = $op->UpdateCourse($_POST['CourseId'] , $_POST['CourseName']);

			
			$response['error'] = false;
			$response['message'] = " updated succefully ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";

		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);


}


function AssignTeacherToCourse()
{
	$response = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{

		if(isset($_POST['Fname']) AND isset($_POST['Lname']) AND isset($_POST['Gyear']) AND isset($_POST['CourseId']))
		{
			$op = new operations();

			$result = $op->TeachIn($_POST['Fname'],$_POST['Lname'],$_POST['Gyear'],$_POST['CourseId']);

			if($result)
			{
				$response['error'] = false;
				$response['message'] = " assigned succefully ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong maybe this teacher is already assigned to this course ";
			}



		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);
	
}


function DoctorsInfo()
{
	$response = array();
	$info = array();
	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$op = new operations();
		$result  = $op->AllDoctors();

		$i = 0;
		while($row = mysqli_fetch_assoc($result))
		{
			$info[$i] = array('Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Gyear' => $row['Gyear'],'Email' => $row['Email'] );
			$i++;

		} 


		$response['error'] = false;
		$response['message'] = " no problem ";

	}
	else
	{

		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	$MainJson = array('info' => $info , 'reponse' => $response);
	echo json_encode($MainJson);

}

function TeacherAssistantsInfo()
{
	$response = array();
	$info = array();
	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$op = new operations();
		$result  = $op->AllTeacherAssistants();

		$i = 0;
		while($row = mysqli_fetch_assoc($result))
		{
			$info[$i] = array('Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Gyear' => $row['Gyear'],'Email' => $row['Email'] );
			$i++;

		} 


		$response['error'] = false;
		$response['message'] = " no problem ";

	}
	else
	{

		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	$MainJson = array('info' => $info , 'reponse' => $response);
	echo json_encode($MainJson);

}

function DoctorsInfoInCourse()
{
	$response = array();
	$info = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		$op = new operations();
		$result  = $op->AllDoctors();

		$i = 0;
		while($row = mysqli_fetch_assoc($result))
		{
			$info[$i] = array('Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Gyear' => $row['Gyear'],'Email' => $row['Email'] );
			$i++;

		} 


		$response['error'] = false;
		$response['message'] = " no problem ";

	}
	else
	{

		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	$MainJson = array('info' => $info , 'reponse' => $response);
	echo json_encode($MainJson);

}

function TeacherInfoInCourse()
{
	$response = array();
	$info = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId'])){
			$op = new operations();
			$result  = $op->GetAllTeachersInCourse($_POST['CourseId']);

			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$info[$i] = array('Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Gyear' => $row['Gyear'] );
				$i++;

			} 	

			$response['error'] = false;
			$response['message'] = " no problem ";

		}

		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}
	
	}
	else
	{

		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	$MainJson = array('info' => $info , 'reponse' => $response);
	echo json_encode($MainJson);

}

function AllCoursesInYear()
{
	$response = array();
	$courses = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['YearNum']) and isset($_POST['Email']))
		{
			$op = new operations();

			$Type = $op->GetTypeOfUser($_POST['Email']);
			
			if($Type == 'D' OR $Type == 'T')
			{
				$teacher = $op->GetTeacher($_POST['Email']);
				$result = $op->GetCourseOfTeacherInYear($teacher['Fname'] , $teacher['Lname'] , $teacher['Gyear'] , $_POST				['YearNum']);	

			}
			else
			{
				$result = $op->AllCoursesInYear($_POST['YearNum']);
			}

			
			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$courses[$i] = array('CourseName' => $row['CourseName'] , 'CourseId' => $row['CourseId'] );
				$i++; 
			}

			$response['error'] = false;
			$response['message'] = " no problem ";


		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{

		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	$MainJson = array('courses' => $courses , 'response' => $response);
	echo json_encode($MainJson);

}

function RepresentativeIdOfYear()
{
	$response = array();
	$Rep ;

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['YearNum']))
		{
			$op = new operations();
			$result = $op->Representative($_POST['YearNum']);
			$row = mysqli_fetch_assoc($result);
			
			$Rep = $row['RepresentativeId'];
			$response['error'] = false;
			$response['message'] = " no problem !! ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}
	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	$MainJson = array( 'RepId' => $Rep , 'response' => $response );
	echo json_encode($MainJson);
}


function AllContactsInYearStudents()
{
	$response = array();
	$contacts = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['YearNum']))
		{
			$op = new operations();
			$result = $op->StudentContact($_POST['YearNum']);
			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$contacts[$i] = array('Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Email' => $row['Email'] , 'Sec' => $row['Sec'] , 'Bn'=>$row['Bn'] , 'Id' => $row['Id']);
				$i++;
			}

			$response['error'] = false;
			$response['message'] = " no problem! ";
		


		}
		else
		{
			$response['error'] = true;
			$response['message'] = " miised fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	$MainJson =array('contacts' =>$contacts , 'response' =>$response);
	echo json_encode($MainJson);



}


function AllContactsInYearTeachers()
{
	$response = array();
	$contacts = array();
	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		
			$op = new operations();
			$result = $op->TeacherContact();
			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$contacts[$i] = array('Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Email' => $row['Email'] , 'Gyear' =>$row['Gyear']);
				$i++;
			}

			$response['error'] = false;
			$response['message'] = " no problem! ";
		


		

	}
	else
	{
		$response['error'] = true;
		$response['message'] = "no problem ! ";
	}
	

	$MainJson =array('contacts' =>$contacts , 'response' =>$response);
	echo json_encode($MainJson);



}


function AddAssignment()
{
	$response = array();
	if( $_SERVER['REQUEST_METHOD']=='POST' )
	{
		if(isset($_POST['CourseId']) AND isset($_POST['Link']) AND isset($_POST['Deadline']) and isset($_POST['SubmitEmail']))
		{
			$op = new operations();
			$result = $op->AddAssignment( $_POST['CourseId'] ,$_POST['Link'] , $_POST['Deadline'],$_POST['SubmitEmail'] );
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " assignment added succefully ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong maybe this assignment already exists !";
			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}


	echo json_encode($response);
}

function UpdateAssignment()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		if(isset($_POST['AssNum']) AND isset($_POST['CourseId']) AND isset($_POST['Link']) AND isset($_POST['Deadline']) and isset($_POST['SubmitEmail']))
		{
			$op = new operations();
			$result = $op->UpdateAssignment($_POST['AssNum'] , $_POST['CourseId'] ,$_POST['Link'] , $_POST['Deadline'] , $_POST['SubmitEmail'] );
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " assignment updated succefully ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong !";
			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}


	echo json_encode($response);
}

function Assignments()
{
	$response = array();
	$Ass = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{

		if(isset($_POST['CourseId']))
		{
			$op = new operations();
			$result = $op->AllAssignmentsInCourse($_POST['CourseId']);
			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$Ass[$i] = array('AssNum' => $row['AssNum'] , 'Link' =>$row['Link'] , 'Deadline' => $row['Deadline'] , 'SubmitEmail'=>$row['SubmitEmail']);
				$i++;

			}

			$response['error'] = false;
			$response['message'] = " no problem !! ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}
		
			


		

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}
	

	$MainJson =array('Ass' =>$Ass , 'response' =>$response);
	echo json_encode($MainJson);

}

function AddOfficeHours()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['Day']) AND isset($_POST['Start']) AND isset($_POST['Until']) AND isset($_POST['Fname']) AND isset($_POST['Lname']) AND isset($_POST['Gyear']))
		{
			$op = new operations();
			$result = $op->AddOfficeHours($_POST['Day'],$_POST['Start'],$_POST['Until'],$_POST['Fname'],$_POST['Lname'],$_POST['Gyear']);
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " added succefully ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong , maybe already exists or this teacher not found !  ";

			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	echo json_encode($response);
}


function DeleteOfficeHours()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['Day']) AND isset($_POST['Start']) AND isset($_POST['Until']) AND isset($_POST['Fname']) AND isset($_POST['Lname']) AND isset($_POST['Gyear']))
		{
			$op = new operations();
			$result = $op->DeleteOfficeHours($_POST['Day'],$_POST['Start'],$_POST['Until'],$_POST['Fname'],$_POST['Lname'],$_POST['Gyear']);
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " deleted succefully ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong ! ";

			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	echo json_encode($response);

}


function GetOfficeHoursOfCourse()
{
	$response = array();
	$teacher = array();
	$OfficeHours = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId']))
		{
			$op = new operations();
			$result = $op->GetAllTeachersInCourse($_POST['CourseId']);
			$i = 0;
			$N = mysqli_num_rows($result);
			while( $row = mysqli_fetch_assoc($result) )
			{
				$teacher[$i] = array( 'Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Gyear' => $row['Gyear'] );
				$i++;
			}


			$i = 0;

			while($i < $N)
			{
				$off = $op->GetOfficeHours($teacher[$i]['Fname'] , $teacher[$i]['Lname'] , $teacher[$i]['Gyear']);
			
				$OfficeHours[$i] = array();
				$j = 0;
				while($row = mysqli_fetch_assoc($off))
				{
					$OfficeHours[$i][$j] = array('Day'=>$row['Day'] , 'Start' => $row['Start'] , 'Until' => $row['Until']);
					$j++;

				}


				$i++;
			}

			$response['error'] = false;
			$response['message'] = " no problem ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	$MainJson = array('teachers'=>$teacher , 'OfficeHours' =>$OfficeHours , 'response' => $response);
	echo json_encode($MainJson);

}


function AddMaterial()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if(isset($_POST['Link'] ) AND isset($_POST['Type']) AND isset($_POST['Topic']) AND isset($_POST['CourseId']))
		{
			$op = new operations();
			$result = $op->AddMaterial($_POST['Topic'] , $_POST['CourseId'] , $_POST['Link'] , $_POST['Type']);
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " added succefully ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong , maybe this material already exists ";

			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);

}

function DeleteContentOfMaterial()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if(isset($_POST['Link'] ) AND isset($_POST['Type']) AND isset($_POST['Topic']) AND isset($_POST['CourseId']))
		{
			$op = new operations();
			$result = $op->DeleteMaterialContent( $_POST['CourseId'] ,$_POST['Topic'] , $_POST['Type'] ,$_POST['Link'] );
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " content deleted succefully ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong !! ";

			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);	
}

function DeleteMaterial()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if( isset($_POST['CourseId']) and isset($_POST['Topic']) )
		{
			$op = new operations();
			$result = $op->DeleteMaterial( $_POST['CourseId'] ,$_POST['Topic'] );
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " material deleted succefully ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong !! ";

			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);

}

function ContentsOfMaterial()
{

	$response = array();
	$contents = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId']))
		{
			$op = new operations();
			$result = $op->ContentsOfMaterial($_POST['CourseId']);
			$i =0;
			while($row = mysqli_fetch_assoc($result))
			{
				$contents[$i] = array('Type' => $row['Type'] , 'Link' => $row['Link'], 'Topic' => $row['Topic']) ;
				$i++;

			}

			$response['error'] = false;
			$response['message'] = " no problem ! ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{

		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	$MainJson = array('contents' => $contents , 'response' => $response);
	echo json_encode($MainJson);

}





function Materials()
{
	$response = array();
	$material = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId']))
		{
			$op = new operations();
			$result = $op->GetMaterialsInCourse($_POST['CourseId']);
			$i =0;
			while($row = mysqli_fetch_assoc($result))
			{
				$material[$i] = $row['Topic'] ;
				$i++;

			}

			$response['error'] = false;
			$response['message'] = " no problem ! ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{

		$response['error'] = true;
		$response['message'] = " invalid request ";

	}

	$MainJson = array('materials' => $material , 'response' => $response);
	echo json_encode($MainJson);
		

}

function UpdateProject()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if(isset($_POST['Title']) AND isset($_POST['CourseId']) AND isset($_POST['Link']) and isset($_POST['MaxNum']) and isset($_POST['SubmitEmail']))
		{
			$op = new operations();
			$result = $op->UpdateProject($_POST['Title'] , $_POST['Link'],$_POST['CourseId'] , $_POST['MaxNum'],$_POST['SubmitEmail']);
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " Updated succefully ! ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong  ! ";

			}


		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";	
	}

	echo json_encode($response);
}


function AddProject()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if(isset($_POST['Title']) AND isset($_POST['CourseId']) AND isset($_POST['Link']) AND isset($_POST['MaxNum']) AND isset($_POST['SubmitEmail']))
		{
			$op = new operations();
			$result = $op->AddProject($_POST['Title'] , $_POST['Link'],$_POST['CourseId'] , $_POST['MaxNum'],$_POST['SubmitEmail']);
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " added succefully ! ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong , maybe this project already exists ! ";

			}


		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";	
	}

	echo json_encode($response);
}




function DeleteProject()
{

	$reponse = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if(isset( $_POST['Title']) AND isset($_POST['CourseId']) )
		{
			$op = new operations();
			$result = $op->DeleteProject(  $_POST['CourseId'] , $_POST['Title']  );
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " Deleted succefully ! ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong ! ";

			}


		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";	
	}

	echo json_encode($response);

}


function GetProjects()
{
	$response = array();
	$projects = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if(isset($_POST['CourseId']))
		{
			$op = new operations();
			$result = $op->AllProjects($_POST['CourseId']);
			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$projects[$i] = array('Title'=>$row['Title'] , 'Link' => $row['Link'] ,'MaxNum' => $row['MaxNum'],'SubmitEmail'=>$row['SubmitEmail']);
				$i++;

			}

			$response['error'] = false;
			$response['message'] = " no problem ! ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";

		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	$MainJson = array('projects' => $projects ,'response' => $response);
	echo json_encode($MainJson);
}

function GetAvailableProjects()
{
	$response = array();
	$projects = array();
	if($_SERVER['REQUEST_METHOD'] =='POST')
	{
		if(isset($_POST['CourseId']) AND isset($_POST['StudentId']))
		{
			$op = new operations();
			$result = $op->AllAvailabeProjects($_POST['CourseId'] , $_POST['StudentId']);
			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$projects[$i] = array('Title'=>$row['Title'] , 'Link' => $row['Link'] ,'MaxNum' => $row['MaxNum'],'SubmitEmail'=>$row['SubmitEmail']);
				$i++;

			}

			$response['error'] = false;
			$response['message'] = " no problem ! ";

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";

		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	$MainJson = array('projects' => $projects ,'response' => $response);
	echo json_encode($MainJson);
}

function SubmitAssignment()
{
	$response = array();
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($_POST['CourseId']) AND isset($_POST['StudentId']) AND isset($_POST['AssNum']))
		{
			$op = new operations();
			$result = $op->SubmitAssignment( $_POST['AssNum'] , $_POST['CourseId'], $_POST['StudentId']   ) ;
			if($result)
			{

				$response['error'] = false;
				$response['message'] = " Submitted ";

			}
			else
			{

				$response['error'] = true;
				$response['message'] = " Something wrong !! " ;

			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);
}

function AllStudentsNotInTeam()
{
	$response = array() ;
	$Students = array() ;
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset( $_POST['CourseId'] ) AND isset( $_POST['Title']) AND isset($_POST['YearId']))
		{
			$op = new operations();
			$result = $op->AllStudentsNotInTeamInProj($_POST['Title'] , $_POST['CourseId'], $_POST['YearId']);
			$i = 0 ;
			while( $row = mysqli_fetch_assoc($result))
			{
				$Students[$i] = array('Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'Email' => $row['Email'] , 'Id' =>$row['Id'],'Sec'=>$row['Sec'],'Bn'=>$row['Bn']);
				$i++; 
			}
			
			$response['error'] = false;
			$response['message'] = " no problem ! " ;

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	$MainJson = array( 'Students' => $Students , 'response' => $response );
	echo json_encode($MainJson);
}

function CreateTeam()
{

	$response = array() ;
	
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset( $_POST['CourseId'] ) AND isset( $_POST['Title']) AND isset($_POST['TeamNumber']) )
		{
			$op = new operations();
			$result = $op->CreateNewTeamInProject($_POST['TeamNumber'] , $_POST['Title'] , $_POST['CourseId']);
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " ok ! choose your members .... ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong maybe this team number already taken ! choose another one .... ";
			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);
}

function AddMemberInTeam()
{
	$response = array() ;
	
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset( $_POST['CourseId'] ) AND isset( $_POST['Title']) AND isset($_POST['StudentId']) AND isset($_POST['TeamNumber']) )
		{
			$op = new operations();
			$result = $op->AddMemberInTeam($_POST['StudentId'] , $_POST['TeamNumber'] , $_POST['CourseId'], $_POST['Title'] );
			if($result == 0)
			{
				$response['error'] = true;
				$response['message'] = " team is full !!! ";
			}
			else if($result ==1)
			{
				$response['error'] = false;
				$response['message'] = "ok.. Added in the team ";
			}
			else if($result == 2)
			{
				$response['error'] = true;
				$response['message'] = " something wrong !!! ";
			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);	

}

function DeleteTeamMember()
{

	$response = array() ;
	
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset( $_POST['CourseId'] ) AND isset( $_POST['Title']) AND isset($_POST['StudentId']) AND isset($_POST['TeamNumber']) )
		{
			$op = new operations();
			$result = $op->DeleteTeamMember($_POST['StudentId'] , $_POST['TeamNumber'] , $_POST['CourseId'], $_POST['Title'] );
			if($result)
			{

				$response['error'] = false;
				$response['message'] = " Delted Succefully ! ";

			}
			else
			{
				$response['error'] = false;
				$response['message'] = " something wrong ! ";
			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);	

}

function StudentsInTeam()
{

	$response = array() ;
	$students = array() ;
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset( $_POST['CourseId'] ) AND isset( $_POST['Title']) AND isset($_POST['TeamNumber']) )
		{
			$op = new operations();
			$result = $op->StudentsInTeam($_POST['Title'] , $_POST['CourseId'] , $_POST['TeamNumber']);
			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$students[$i] = array('Fname' => $row['Fname'] , 'Lname' => $row['Lname'] , 'YearNum' => $row['YearId'] , 'Sec'=> $row['Sec'] , 'Bn' => $row['Bn'] , 'Id' => $row['Id']);
				$i++;

			}

			$response['error'] = false;
			$response['message'] = " no problem !!... ";
		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}
	$MainJson = array('students'=>$students , 'response' => $response);

	echo json_encode($MainJson);	

}




function TeamsInProj()
{
	$response = array();
	$teams = array();
	
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset( $_POST['CourseId'] ) AND isset( $_POST['Title']) )
		{
			$op = new operations();
			$result = $op->TeamsInProj($_POST['CourseId'], $_POST['Title']);
			$i = 0;
			while($row = mysqli_fetch_assoc($result))
			{
				$teams[$i] = $row['TeamNumber'];
				$i++;

			}

			$response['error'] = false;
			$response['message'] = " no problem ! ";
		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	$MainJson = array('teams' => $teams , 'response' => $response);
	echo json_encode($MainJson);	

}


function DeleteTeam()
{
	$response = array() ;
	
	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset( $_POST['CourseId'] ) AND isset( $_POST['Title']) AND isset($_POST['TeamNumber']) )
		{
			$op = new operations();
			$result = $op->DeleteTeam($_POST['TeamNumber'] , $_POST['CourseId'], $_POST['Title'] );
			if($result)
			{
				$response['error'] = false;
				$response['message'] = " Team Deleted ! ";
			}
			else
			{
				$response['error'] = true;
				$response['message'] = " something wrong ! ";
			}

		}
		else
		{
			$response['error'] = true;
			$response['message'] = " missed fields ";
		}

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}

	echo json_encode($response);	

}

function YearsInfo()
{
	$response = array();
	$years = array();
	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$op = new operations();
		$result = $op->Years();
		$i = 0;
		while($row = mysqli_fetch_assoc($result))
		{
			$years[$i] = array('YearNum' => $row['YearNumber'] , 'Gyear' => $row['Gyear'] , 'Representative' => $row['RepresentativeId'] ,'NumOfStudents' => $row['NumOfStudents']);
			$i++;
		}

		$response['error'] = false;
		$response['message'] = " no problem ! ";

	}
	else
	{
		$response['error'] = true;
		$response['message'] = " invalid request ";
	}
	$MainJson = array('years' => $years , 'response' => $response);
	echo json_encode($MainJson);
}

//Statistics of the year

function Count_Statistics()
{
	$Count = array();
	$op = new operations();

	//number of students in year...........

	$result1 = $op->NumberOfStudentsInYear($_POST['YearNum']);
	$Count['CountStudents'] = $result1;

	//number of Courses in year...........

	$result2 = $op->NumberOfCoursesInYear($_POST['YearNum']);
	$Count['CountCourses'] = $result2;

	//number of Projects in year...........

	$result3 = $op->NumberOfProjectsInYear($_POST['YearNum']);
	$Count['CountProjects']=$result3;

	//number of Doctors in year...........

	$result4 = $op->NumberOfDoctors($_POST['YearNum']);
	$Count['CountDoctors'] = $result4;
	
	//number of TeacherAss in year...........

	$result5 = $op->NumberOfTeacherAssistants($_POST['YearNum']);
	$Count['CountAssistants'] = $result5;	



	echo json_encode($Count);

}

function Statistics_group()
{
	$Count = array();
	$op = new operations();

	//number of assignments in Courses in year

	$Count['Assignments'] =array();
	$i = 0;
	$result1 = $op->NumberOfAssignmentsInCoursesInYear($_POST['YearNum']);
	while( $row = mysqli_fetch_assoc($result1) )
	{
		$Count['Assignments'][$i] = array('CourseName'=>$row['CourseName'],'NumOfAss' =>$row['CountAss'] );
		$i++;
	}

	//number of teams in project.....

	$Count['Teams'] =array();
	$i = 0;
	$result2 = $op->NumberOfTeamsOfProjectsInYear($_POST['YearNum']);
	while( $row = mysqli_fetch_assoc($result2) )
	{
		$Count['Teams'][$i] = array('ProjectName'=>$row['Title'],'NumOfTeams' =>$row['CountTeams'] );
		$i++;
	}

	//numberofDoctorsAndTeacherassistants in course

	$Count['Teachers'] =array();
	$i = 0;
	$result3 = $op->NumberOfTeachersInCourse($_POST['YearNum']);
	while( $row = mysqli_fetch_assoc($result3) )
	{
		$Count['Teachers'][$i] = array('CourseName'=>$row['CourseName'],'NumOfTeachers' =>$row['CountDoc'] );
		$i++;
	}




	echo json_encode($Count);




}


