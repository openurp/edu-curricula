[#ftl]
{
  "courses" : [
                [#list courses! as course]{ "id" : "${course.id}", "name" : "${course.name?js_string}", "code" : "${course.code?js_string}", "credits": ${course.credits}, "period": ${course.creditHours}, "weekHours": ${course.weekHours}, "weeks": ${course.weeks}}[#if course_has_next],[/#if][/#list]
              ]
}
