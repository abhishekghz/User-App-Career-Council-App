package com.sunny.careercouncilapp

class Model(course_image: Int) {

    private var course_image = 0

    // Constructor
    init {
        this.course_image = course_image
    }


    // Getter and Setter


    fun getCourse_image(): Int {
        return course_image
    }

    fun setCourse_image(course_image: Int) {
        this.course_image = course_image
    }
}