## SRS

1.	When starting the application, a user can choose whether to login or register as a new student: 

•	Login: The student enters a username.  The app determines if the student is a current user.  If true, the student is logged in and if false, the student is taken to a registration menu.

•	Register: The student is taken to a registration menu and enters their student information (username, major, seniority Level, and email). 

2.	Menu: Once the student is logged in, the application gives the user the choice to add a quiz, remove a quiz, practice a quiz, view statistics, or logout.

3.	If the user chooses to add a quiz:

•	The user is prompted to enter a unique name and description.  

•	The User is prompted to add between N words (where N is between 1 and 10), N correct definitions associated with the words, and 3*N incorrect definitions which are saved under the unique Quiz the user just created.  

4.	If the user chooses to remove a quiz:

•	The user is given a list of quizzes the user created and the user selects one to delete.

o	The user Quiz instance is removed from the quizzes.

o	The quiz statistics for the particular quiz are removed from quiz Statistics 

5.	If the user chooses to practice a quiz:

•	The user selects a quiz from a dropdown menu.  The dropdown menu displays the quizzes created by other students.

•	If there are no quizzes created by other users, an error message is displayed indicating there are no quizzes available to practice.

6.	A current Practice Session begins when a user selects the quiz to practice.

•	Questions appear one at a time.  A word is displayed with four options for the definition (displayed in random order).

•	The user selects an answer.

o	The response is determined as correct or incorrect

•	The user can view the number correct/total answered at the top of the page, under the quiz name.

•	Once all N questions have been displayed and responded to, the final score is calculated and the quiz statistics (unique name, username, score, date/time played) are recorded in the quiz Statistics.

7.	If the user chooses to view statistics:

•	A list of all quizzes taken by the user are displayed in order of date (most recent first).

o	If the user clicks on a quiz: 

	The user sees the first score they received and when it was achieved.

	The user also sees the highest score they received and when it was achieved.

	Lastly the user sees the usernames of the first three students to score 100% on the quiz, listed by username in alphabetical order.



