* When starting the application, a user can choose whether to (1) log in as a specific student or (2) register as a new student.
  - To register as a new student, the user must provide the following student information:
    -  A unique username
    - A major
    - A seniority level (i.e., freshman, sophomore, junior, senior, or grad)
    - An email address  
  - The newly added student is immediately created in the system.
  - For simplicity, there is no password creation/authentication; that is, selecting or entering a student username is sufficient to log in as that student.
  - Also for simplicity, student and quiz information is local to a device.

To meet this requirement, I started with a basic **Student** class, with the attributes _username_, _major_, _seniorityLevel_, and _emailAddress_. I also gave this class the functions _addQuiz_, _removeQuiz_, _practiceQuiz_, and _viewScores_ based on this requirement. However, my intention was for these functions to primarily interact with the GUI, and so each function has a void return type and relies on the **QuizMaster** to execute most of the logic in these functions.

---

 * The application allows students to (1) add a quiz, (2) remove a quiz they created, (3) practice quizzes, and (4) view the list of quiz score statistics.
 
To meet this requirement, I created the **Quiz** class and created a dependency of **Student** on **Quiz**, as the Student's _addQuiz_ function takes a **Quiz** as a parameter. I also created the concept of the **QuizMaster** class, which maintains the list of **Quiz** objects. In doing so, I implement the relationships described here while placing the methods not in instances of either the **Student** or **Quiz**, but in a centralized location.

--- 

* To add a quiz, a student must enter the following quiz information:
  - Unique name
  - Short description
  - List of N words, where N is between 1 and 10,  together with their definitions 
  - List of N * 3 incorrect definitions, not tied to any particular word, where N is the number of words in the quiz.

This requirement allowed me to fill in the attributes for the **Quiz** class; namely, _name_ and _description_. The words and definitions are implemented in my design as two object lists, one of the **Word** class and one of the **Definition** class. The length of the definition list, then, is constrained to be exactly three times the size of the word list.

Each of these classes simply has a string attribute _value_, as each is really just a wrapper for a string in a particular context.

---

* To remove a quiz, students must select it from the list of the quizzes they created. Removing a quiz must also remove the score statistics associated with that quiz.

This requirement pushed me to think about how the **QuizMaster** might organize its **Quiz** objects such that students might quickly find quizzes that they're interested in, but did not lead to any design changes. The **QuizMaster** functions will be used to manage its lists of **Quiz** and **Score** objects, including removing quizzes and scores.

I considered at this point adding an attribute to the **Quiz** class to indicate who created that quiz, but ultimately decided against this as I believe the **QuizMaster** can track this information as it recieves requests from students to add and remove quizzes.

---

* To practice a quiz, students must select it from the list of quizzes created by other students.

This requirement makes quite explicit the idea that quizzes should not be members of any particular **Student** object, as other students must access these quizzes. Instead, students will make requests to the **QuizMaster** which will internally manage quizzes and serve them up to students who wish to take them.
 
---

* When a student is practicing a quiz, the application must do the following:
  - Until all words in the quiz have been used in the current practice session: 
    - Display a random word in the quiz word list.
    - Display four definitions, including the correct definition for that word (the other three definitions must be randomly selected from the union of (1) the set of definitions for the other words in the quiz and (2) the set of incorrect definitions for the quiz. 
    - Let the student select a definition and display “correct” (resp., “incorrect”) if the definition is correct (resp., incorrect).
  - After every word in the quiz has been used, the student will be shown the percentage of words they correctly defined, and this information will be saved in the quiz score statistics for that quiz and student.

The requirements here concern the concept of a particular quiz session. However, rather than introduce a class to represent quiz sessions, in my design the **QuizMaster** is responsible for maintaining the state of the current quiz session. In pseudocode, the process of the master managing state might look something like this:

```
class Gui:
  . . .
  onButtonClick():
    QuizMaster.practiceQuiz(currentStudent, selectedQuizName)
. . .
class QuizMaster:
  . . .
  practiceQuiz(student, quizName):
	quiz = getQuiz(student, quizName)
    correct = 0
    for(i <- 0 to quiz.words.length):
      answer = Gui.promptQuestion(
          quiz.words(i).value, quiz.words(i).definition, quiz.defintions(i * 3), 
          quiz.defintions(i * 3 + 1), quiz.defintions(i * 3 + 2)
      )
      if(answer == quiz.words(i).definition):
          correct += 1
    return score(student, now(), correct / quiz.words.length)
  . . .
```

---

* The list of quiz score statistics for a student must list all quizzes, ordered based on when they were last played by the student (most recent first). Clicking on a quiz must display (1) the student’s first score and when it was achieved (date and time), (2) the student’s highest score and when it was achieved (date and time), and (3) the names of the first three students to score 100% on the quiz, ordered alphabetically.

This requirement deals largely with the behavior of the GUI. The information here suggests that it must be possible to quickly retrieve scores accociated with either a particular student or quiz. Exporting the list of scores to the **QuizMaster** allows us to do that without having to iterate through the entire list of students or quizzes. If we were to implement this design with a database, this requirement also informs how we might index that database.
 
---

* The user interface must be intuitive and responsive.

This requirement is also GUI-related. The layout of the GUI is outside the scope of this design, but the restriction that the GUI must be responsive informs how we might organize our GUI.

---

* The performance of the game should be such that students do not experience any considerable lag between their actions and the response of the application.

This requirement is also GUI-related and suggests that lookups must be quick. If we use appropriate data structures to be able to look up students, quizzes, and scores, we can accomplish this with the design as currently described. 
