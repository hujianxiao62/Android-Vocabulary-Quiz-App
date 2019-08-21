## Notion
- **Class**  
- Class.attribute
- Class.operation()
- _relationship_

## Requirements
1. When starting the application, a user can choose whether to (1) log in as a specific student or (2) register as a new student.
    1. To register as a new student, the user must provide the following student information:
        1. A unique username
        1. A major
        1. A seniority level (i.e., freshman, sophomore, junior, senior, or grad)
        1. An email address
    1. The newly added student is immediately created in the system.
    1. For simplicity, there is no password creation/authentication; that is, selecting or entering a student username is sufficient to log in as that student.
    1. Also for simplicity, student and quiz information is local to a device.

- The **Student** class is added with above attributes of Student.userName, Student.major, Student.senority, Student.email. 
- A Boolean type attribute, Student.loginStatus is also added which switched by Student.login(), Student.register(), Student.logout(). 
- To check the uniqueness of the username, the **CheckString** and CheckString.isSame() are created and used by the Student.login(), Student.register().    
- Requirement 1.ii and 1.iv are not represented in the design for they handle purely database implementation.


2. The application allows students to (1) add a quiz, (2) remove a quiz they created, (3) practice quizzes, and (4) view the list of quiz score statistics.

- This requirement is detailed in requirement 3, 4, 5, 6, 7.


3. To add a quiz, a student must enter the following quiz information:
    1. Unique name
    1. Short description
    1. List of N words, where N is between 1 and 10,  together with their definitions 
    1. List of N * 3 incorrect definitions, not tied to any particular word, where N is the number of words in the quiz.

- The **Question** class is added with Question.questionWord to contain a test word and Question.rightAnswer to contain its definition. 
- The **Quiz** class is an _aggregation_ of 1-10 **Question** that have attributes Quiz.quizName, Quiz.description, Quiz.provider and Quiz.wrongAnswer. 
- Student.addQuiz() is added to implement the requirement. Thus **Student** and **Quiz** are associated as _AddQuiz_. 
- To verify the qualification of Quiz.quizName and Quiz.wrongAnswer,  **CheckString** is _used_ by **Student** and **Quiz**.
- **GlobalScore** is added to document the quiz related statistics that could be accessed by all logined user. **InitialScore** is a _sub-class_ of **GlobalScore** that will be created by **Student**. initializeScore() when Student.addQuiz() is applied. Thus **Student** and **InitialScore** are associated as _InitializeScore_.


4. To remove a quiz, students must select it from the list of the quizzes they created. Removing a quiz must also remove the score statistics associated with that quiz.

- Student.removeQuiz() is added to realize the requirement. Thus **Student** and **Quiz** are also associated as _RemoveQuiz_. **CheckString** is also used to check the existence of the certain quiz.
- Student.removeScore() is added and could be applied along with Student.removeQuiz() to remove corresponding quiz data. Thus **Student** and **GlobalScore** are associated as _RemoveScore_.
- **CheckString** is _used_ by **Student** to verify Student.userName and Quiz.provider are same.  


5. To practice a quiz, students must select it from the list of quizzes created by other students.

- Student.practiceScore() is added to fulfill the requirement. Thus **Student** and **Quiz** are also associated as _PracticeQuiz_.  Quiz.userName() is added to return the current Student.userName.
- **CheckString** is used by **Student** to verify Student.userName and Quiz.provider are different. 


6. When a student is practicing a quiz, the application must do the following:
    1. Until all words in the quiz have been used in the current practice session: 
        1. Display a random word in the quiz word list.
        1. Display four definitions, including the correct definition for that word (the other three definitions must be randomly selected from the union of (1) the set of definitions for the other words in the quiz and (2) the set of incorrect definitions for the quiz. 
        1. Let the student select a definition and display “correct” (resp., “incorrect”) if the definition is correct (resp., incorrect).
    1. After every word in the quiz has been used, the student will be shown the percentage of words they correctly defined, and this information will be saved in the quiz score statistics for that quiz and student.

- To display the random word and random incorrect definitions, Quiz.randomQuestion() and Quiz.randomWrong() are added. 
- The practice score and date are calculated by Quiz.accountScore() and Quiz.practiceDate(). These data will be recorded in **PracticeScore** by Quiz.writeScore(). Thus **Quiz** and **PracticeScore** has a _WriteScore_ association. 
- **PracticeScore** is another _sub-class_ of **GlobalScore** to record the PracticeScore.userName, PracticeScore.firstScore, PracticeScore.firstPlayedDate, PracticeScore.highestScore, PracticeScore.lastPlayedDate.
- The display function of requirement 6.a.i,ii,iii are not state here as will be handled in GUI.


7. The list of quiz score statistics for a student must list all quizzes, ordered based on when they were last played by the student (most recent first). Clicking on a quiz must display (1) the student’s first score and when it was achieved (date and time), (2) the student’s highest score and when it was achieved (date and time), and (3) the names of the first three students to score 100% on the quiz, ordered alphabetically.

- The **Student** and **GlobalScore** are associated as _CheckScore_ by Student.checkScore(). 
- For the quizzes created by user and have not been practiced, only global info (GlobalScore.quizName, GlobalScore.topPlayer and GlobalScore.topNumber) could be accessed. Because only **InitialScore** is created when implement Student.addQuiz and Student.intializeScore.
- For the quizzes that have been practiced, both global info and practice info (PracticeScore.userName, PracticeScore.firstScore, PracticeScore.firstPlayedDate, PracticeScore.highestScore, PracticeScore.lastPlayedDate) could be accessed through **PracticeScore**.
- GlobalScore.setTopPlayer is added to updated the first three students to score 100% on the quiz.


8. The user interface must be intuitive and responsive.

- This requirement is not represented as will be handled in GUI.


9. The performance of the game should be such that students do not experience any considerable lag between their actions and the response of the application.

- This requirement is not represented as will be handled in GUI.

