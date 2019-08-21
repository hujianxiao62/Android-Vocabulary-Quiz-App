#Design Information on Quiz App
## sanderson302

1.	Student Class: When starting the application, a user can choose whether to login or register as a new student: 

•	Login: The student enters a username.  The checkIfUser() method determines if the student is in the userList.  If true, the student is logged in through a login() method and if false, an error message is returned and the user has the option of selecting login or register again.

•	Register: The student is taken to a registration menu through the register() method and enters their student information through the enterUserInfo() method (note that getters and setters are not shown for username, major, seniorityLevel, and email).  The student is returned to the login menu and enters the username (see 1a).

2.	User class: Once the student is logged in, the application gives the user the choice to add a quiz, remove a quiz, practice a quiz, view statistics, or logout.

3.	If the user chooses to add a quiz:

•	The user is prompted to enter a uniqueName and description through the enterQuizInfo() method.  The new quiz is saved as a unique userQuiz instance.

•	User is prompted to add between N words (where N is between 1 and 10), N correct definitions associated with the words, and 3*N incorrect definitions which are saved under the unique userQuiz just created.  

•	The unique username is added as a characteristic of the userQuiz on creation.

•	The number of words is counted in noWords() method and the integer is stored as totalPoints.

•	The userQuiz instance is added to the quizList array.		

4.	If the user chooses to remove a quiz:

•	The user is given a list of userQuizzes from the array and the user selects one to delete.

**	The userQuiz instance is removed from allQuiz array and the userQuiz array.

**	The quiz statistics for the particular quiz are removed from quizStatistics 

5.	If the user chooses to practice a quiz:

•	The user selects an otherQuiz instance from non-user quizzes array (formed by nonUserQuiz() method which separates out any quizzes which have the user’s username listed as the creator).

•	If no “otherQuiz” instance, or quiz created by other users, is available, an error message is displayed indicating there are no quizzes available to practice.

6.	A currentPracticeSession begins when a user selects the quiz to practice.

•	Questions appear one at a time through the displayQuestion() method in the currentPracticeSession.  A word is displayed with four options for the definition (displayed in random order).

**	The question displayed in the currentPracticeSession is populated through the questions class.  The createQuestionsList() method in the questions class loops through 1 through N words associated with the otherQuiz instance and for each word selects the correct definition and three other definitions from the union of the incorrectDefinition array and the remaining correct definitions (created by unionIncorrectDefs() method in the questions class).

•	The user selects an answer (userResponse class).

**	The response is determined as correct or incorrect in the currentPracticeSession through the isRight() method.

**	The user can view whether or not the current answer is correct through the displayCorrect() method.

•	The user can view the number correct/total answered through the displayScore() method

•	Once all N questions have been displayed and responded to, the finalScore() is calculated and the quiz statistics (uniqueName, username, score, date/time played) are recorded in quizStatistics

7.	If the user chooses to view statistics:

•	A list of all quizzes taken by the user are displayed in order of date (most recent first) using sortByUserDate() in the quizStatistics class.

**	If the user clicks on a quiz: 

***	The user sees the first score they received and when it was achieved through the displayStats() method.  The firstUserScore() method finds all instances of quizzes with the particular uniqueName and uses sortByUserDate() method to find the earliest statistics.

***	The user also sees the highest score they received and when it was achieved through the displayStats() method.  The highUserScore() method finds all instances of quizzes with the particular uniqueName and uses sortByUserScore() method to find the highest score of that particular user.

***	Lastly the user sees the usernames of the first three students to score 100% on the quiz through the displayStats() method.  The topThree100() method finds all quiz scores of 100% for the particular quiz, sorts them by date, then lists the first three usernames in alphabetical order.

8.	The user interface must be intuitive and responsive.

•	This is not represented in my design as it will be handled entirely within the GUI implementation.

9.	The performance of the game should be such that students do not experience any considerable lag between their actions and the response of the application.

•	A quiz engine was created through the questions class and though it may take a few seconds to load a practice quiz, the response time should be minimal.  One way to minimize the delay would to not recreate the quiz each time (definitions would always be listed in the same order for every user); however, this is not how I understood the directions of the assignment.



