Student/User class

All have student with same 4 attributes for Student Class: username, major, seniority, email, 
JH: Defines a user though login status
LL and SA: have a separate User class
Simpler not to have a separate user class.  Simple app like this, not necessary to have another class for user.

Student.methods:  

Similar: Add, remove, practice quiz or view stats
JH:  Different: login, register, logout, initialize score, remove Score, 
LL: Get and sets in Student, in User - Add, remove, practice, quiz or view stats, also main (what does that method do?) login, addstudent (register)
EM: Add, remove, practice, quiz or view stats
SA: student: login, register, enterinfo, saveasuser, User: Add, remove, practice, quiz or view stats + logout

METHODS: 

Add, remove, practice, quiz or view stats
(login, register in GUI??)
No logout

JH Discussion on initializing the score: when a student creates a quiz, the globalscore gets initial data from the newly created quiz

DISCUSSION ABOUT HOW QUIZ STATISTICS ARE STORED AND ACCESSED:

EM: Perhaps we need a quiz statistics class.  Maybe discuss why a score list is kept in the QuizMaster class.
JH: Class for global scores (View Statistics for student), PracticeScore – current session score and the highest and first score is kept
LL: The Student and Quiz both appear to be giving the quiz score data to the QuizScore class; however; only the Quiz should be giving this data.

We think that all quizzes (whether or not they have been played) should be listed in quiz statistics.  If a student selects a quiz which has no statistics, they will be shown as “Empty”.  The quiz name will be there, but it will not have data.

3 main components:

USER/STUDENT – one class

QUIZ – EM MasterQuiz, SA CurrentQuizSession, We think we should have one.

SCORES

JH:
Quiz Class: (checkout the methods, maybe don’t need them)
practiceDate - remove
Writescore(): returns a practice score

GlobalScore Class: (checkout the mthods)
No need for top player

Maybe combine PracticeScore and GlobalScore into one class called Score