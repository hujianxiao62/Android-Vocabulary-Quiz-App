package edu.gatech.seclass.sdpvocabquiz.espresso

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import edu.gatech.seclass.sdpvocabquiz.R
import edu.gatech.seclass.sdpvocabquiz.activity.LoginActivity
import edu.gatech.seclass.sdpvocabquiz.adt.Quiz
import edu.gatech.seclass.sdpvocabquiz.adt.Student
import org.hamcrest.core.AllOf
import org.hamcrest.core.Is
import org.hamcrest.core.IsInstanceOf
import android.support.test.espresso.Espresso.onData

import android.support.test.espresso.Espresso.onView
import android.support.test.rule.ActivityTestRule
import edu.gatech.seclass.sdpvocabquiz.adt.Score
import edu.gatech.seclass.sdpvocabquiz.adt.Word
import edu.gatech.seclass.sdpvocabquiz.database.DatabaseManager
import edu.gatech.seclass.sdpvocabquiz.database.SqliteDatabaseManager
import org.hamcrest.Matchers.anything
import org.junit.*

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not


class SystemTest {

    private lateinit var db: DatabaseManager

    @Before
    fun setUp() {
        db = SqliteDatabaseManager(InstrumentationRegistry.getTargetContext())
        resetDatabase()
    }

    @After
    fun finish() {
        resetDatabase()
    }

    fun resetDatabase() {
        db.dropTables()
        db.createTables()
    }


    @get:Rule
    var testActivityRule = ActivityTestRule<LoginActivity>(
            LoginActivity::class.java)

    @Test
    // input registered userName and enter Menu
    // logout at Menu
    fun login_logout1() {

        resetDatabase()
        val student = Student("Sasha", "m", "s", "e")
        db.putStudent(student)

        onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("Sasha"), ViewActions.closeSoftKeyboard())

        Espresso.closeSoftKeyboard()

        onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())

        onView(ViewMatchers.withId(R.id.txt_Menu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.btn_MenuLogout)).perform(ViewActions.click())

        onView(withText(R.string.toast_Login_Success)).inRoot(withDecorView(not(testActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.txt_loginUsername)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    // input new userName and enter registerPage
    fun login2() {

        resetDatabase()

        Espresso.onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("Eric"), ViewActions.closeSoftKeyboard())

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.RegisterText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    // input register data and enter Menu
    fun registerTestCase1() {

        Espresso.onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("Jianxiao"), ViewActions.closeSoftKeyboard())

        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_registerMajor)).perform(ViewActions.clearText(), ViewActions.typeText("ME"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.txt_reigsterEmail)).perform(ViewActions.clearText(), ViewActions.typeText("jhu369@Gatech.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.spinner_registerSeniority)).perform(ViewActions.click())
        Espresso.onData(AllOf.allOf(Is.`is`(IsInstanceOf.instanceOf<Any>(String::class.java)), Is.`is`("Freshman"))).perform(ViewActions.click())

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btn_registerDone)).perform(ViewActions.click())

        onView(withText(R.string.toast_Register_Success)).inRoot(withDecorView(not(testActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.txt_Menu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    // register info inserted in db
    fun registerTestCase2() {
        resetDatabase()

        Espresso.onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("Jianxiao"), ViewActions.closeSoftKeyboard())

        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_registerMajor)).perform(ViewActions.clearText(), ViewActions.typeText("ME"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.txt_reigsterEmail)).perform(ViewActions.clearText(), ViewActions.typeText("jhu369@Gatech.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.spinner_registerSeniority)).perform(ViewActions.click())
        Espresso.onData(AllOf.allOf(Is.`is`(IsInstanceOf.instanceOf<Any>(String::class.java)), Is.`is`("Sophomore"))).perform(ViewActions.click())

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btn_registerDone)).perform(ViewActions.click())

        onView(withText(R.string.toast_Register_Success)).inRoot(withDecorView(not(testActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.txt_Menu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Assert.assertEquals("Sophomore", db.getStudent("Jianxiao")?.seniorityLevel)
        Assert.assertEquals("jhu369@Gatech.com", db.getStudent("Jianxiao")?.emailAddress)
        Assert.assertEquals("ME", db.getStudent("Jianxiao")?.major)
    }

    @Test
    // add a quiz
    fun addQuizTestCase1() {

        resetDatabase()
        val student = Student("Jianxiao", "m", "s", "e")
        val quiz1 = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))

        db.putStudent(student)

        Espresso.onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("Jianxiao"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_MenuAddQuiz)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addQuizTitle)).perform(ViewActions.clearText(), ViewActions.typeText("foo"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addQuizDescription)).perform(ViewActions.clearText(), ViewActions.typeText("bar"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_addQuizNext)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWordsWord)).perform(ViewActions.clearText(), ViewActions.typeText("wing"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWordsDef)).perform(ViewActions.clearText(), ViewActions.typeText("ding"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_AddWordsAdd)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_AddWordsDone)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWrongDef)).perform(ViewActions.clearText(), ViewActions.typeText("a"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_AddWrongAdd)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWrongDef)).perform(ViewActions.clearText(), ViewActions.typeText("b"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_AddWrongAdd)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWrongDef)).perform(ViewActions.clearText(), ViewActions.typeText("c"), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.btn_AddWrongAdd)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_AddWrongDone)).perform(ViewActions.click())

        onView(ViewMatchers.withId(R.id.txt_Menu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Assert.assertEquals(quiz1, db.getQuiz("foo"))
    }

    @Test
    // add two same wrongDef same as Def
    fun addQuizTestCase2() {

        resetDatabase()
        val student = Student("Jianxiao", "m", "s", "e")
        val quiz1 = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))

        db.putStudent(student)

        Espresso.onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("Jianxiao"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_MenuAddQuiz)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addQuizTitle)).perform(ViewActions.clearText(), ViewActions.typeText("foo"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addQuizDescription)).perform(ViewActions.clearText(), ViewActions.typeText("bar"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_addQuizNext)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWordsWord)).perform(ViewActions.clearText(), ViewActions.typeText("wing"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWordsDef)).perform(ViewActions.clearText(), ViewActions.typeText("ding"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_AddWordsAdd)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_AddWordsDone)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWrongDef)).perform(ViewActions.clearText(), ViewActions.typeText("a"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_AddWrongAdd)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addWrongDef)).perform(ViewActions.clearText(), ViewActions.typeText("a"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_AddWrongAdd)).perform(ViewActions.click())
        onView(withText(R.string.toast_You_ve_already_used_that_definition)).inRoot(withDecorView(not(testActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()))

    }

    @Test
    // add a exist quiz
    fun addQuizTestCase3() {

        resetDatabase()
        val student = Student("Jianxiao", "m", "s", "e")
        val quiz1 = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))

        db.putStudent(student)
        db.putQuiz(quiz1)
        Espresso.onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("Jianxiao"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_MenuAddQuiz)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addQuizTitle)).perform(ViewActions.clearText(), ViewActions.typeText("foo"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.txt_addQuizDescription)).perform(ViewActions.clearText(), ViewActions.typeText("bar"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_addQuizNext)).perform(ViewActions.click())
        onView(withText(R.string.toast_A_quiz_with_that_name_already_exists)).inRoot(withDecorView(not(testActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()))
    }


    @Test
    // remove quiz
    fun RemoveQuizTestCase1() {

        resetDatabase()
        val student = Student("u", "m", "s", "e")
        val quiz1 = Quiz(student, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        db.putStudent(student)
        db.putQuiz(quiz1)

        onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("u"), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_menuRemoveQuiz)).perform(ViewActions.click())

        //click with name
        onData(anything("foo")).inAdapterView(ViewMatchers.withId(R.id.txt_list_element)).perform(click())

        //click at position
        //onData(anything()).inAdapterView(ViewMatchers.withId(R.id.txt_list_element)).atPosition(0).perform(click())

        Assert.assertEquals(null, db.getQuiz("foo"))
    }

    @Test
// pracitce quiz
    fun PracticeQuizTestCase1() {

        resetDatabase()
        val student1 = Student("u", "m", "s", "e")
        val student2 = Student("v", "w", "x", "z")

        val quiz1 = Quiz(student2, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        db.putStudent(student1)
        db.putStudent(student2)
        db.putQuiz(quiz1)

        onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("u"), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_MenuPracticeQuiz)).perform(ViewActions.click())
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.txt_list_element)).atPosition(0).perform(click())
        onView(ViewMatchers.withId(R.id.practice_one)).perform(ViewActions.click())

        onView(ViewMatchers.withId(R.id.txt_Menu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Assert.assertNotEquals(db.getScores("u","foo"), null)
    }

    @Test
// pracitce quiz
    fun ViewScoreTestCase1() {

        resetDatabase()
        val student1 = Student("u", "m", "s", "e")
        val student2 = Student("v", "w", "x", "z")
        val quiz1 = Quiz(student2, "foo", "bar", listOf<Word>(Word("wing", "ding")), listOf<String>("a", "b", "c"))
        val score = Score(student1,quiz1, 100.0,100 )
        db.putStudent(student1)
        db.putStudent(student2)
        db.putQuiz(quiz1)
        db.putScore(score)

        onView(ViewMatchers.withId(R.id.txt_loginUsername)).perform(ViewActions.clearText(), ViewActions.typeText("u"), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.btn_loginGo)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_MenuViewScore)).perform(ViewActions.click())
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.txt_list_element)).atPosition(0).perform(click())

        onView(ViewMatchers.withId(R.id.view_score_quiz)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

}


