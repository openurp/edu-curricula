[#ftl]
[@b.head/]
<script language="JavaScript" type="text/JavaScript" src="${base}/static/js/ajax-chosen.js"></script>
[@b.toolbar title="修订任务"]bar.addBack();[/@]
  [#assign sa][#if reviseTask.persisted]!update?id=${reviseTask.id}[#else]!save[/#if][/#assign]
    [@b.form action=sa theme="list" ]
      [#if reviseTask.persisted]
        [@b.field label="学年学期"]${reviseTask.semester.schoolYear}学年${reviseTask.semester.name}学期[/@]
        [@b.field label="课程"]${reviseTask.course.name}(${reviseTask.course.code})[/@]
      [#else ]
        [@urp_base.semester name="reviseTask.semester.id" label="学年学期"  value=currentSemester required="true"/]
        [@b.field label="选择课程" required='true']
          <select id="courses" name="reviseTask.course" style="width:200px;" >
            <option value="${(reviseTask.course.code)!}" selected>${(reviseTask.course.name)!}${(reviseTask.course.code)!}</option>
          </select>
        [/@]
      [/#if]
      [#if reviseTask.persisted && reviseTask.teachers ?? && reviseTask.teachers?size>0]
        [@b.field label="任课教师"]
          [#list reviseTask.teachers as teacher]
            ${teacher.name}[#if teacher_has_next],[/#if]
          [/#list]
        [/@]
        [@b.field label="选择负责人"]
          <select id="users" name="reviseTask.author" style="width:200px;" >
            <option value="">...</option>
            [#if reviseTask.author ??]
              [#list reviseTask.teachers as teacher]
                <option value="${(teacher.code)!}" [#if reviseTask.author == teacher]selected[/#if]>${(teacher.name)!}(${(teacher.code)!})</option>
              [/#list]
            [#else ]
              <option value="${(reviseTask.author.code)!}" selected>${(reviseTask.author.name)!}${(reviseTask.author.code)!}</option>
              [#list reviseTask.teachers as teacher]
                <option value="${(teacher.code)!}">${(teacher.name)!}(${(teacher.code)!})</option>
              [/#list]
            [/#if]
          </select>
        [/@]
      [#else]
        [@b.field label="选择负责人"]
          <select id="users" name="reviseTask.author" style="width:200px;" >
            <option value="">...</option>
            <option value="${(reviseTask.author.code)!}" selected>${(reviseTask.author.name)!}${(reviseTask.author.code)!}</option>
          </select>
        [/@]
      [/#if]
      [@b.formfoot]
        [#if reviseTask.persisted]<input type="hidden" name="reviseTask.id" value="${reviseTask.id!}" />[/#if]
        [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
        [#list 1..10 as i] <br> [/#list]
      [/@]
    [/@]

<script>
  jQuery(function() {
    jQuery("#courses").ajaxChosen(
            {
              method: 'POST',
              url: '${b.url("course-or-user-search!courseAjax")}',
              postData:function(){
                return {
                  pageIndex:1,
                  pageSize:10,
                }
              }
            }
            , function(data) {
              var dataObj=eval("(" + data + ")");
              var items = {};
              jQuery.each(dataObj.courses, function(i, course) {
                items[course.code] = course.name + '(' + course.code + ')';
              });
              return items;
            });

  })

  jQuery(function() {
    jQuery("#users").ajaxChosen(
            {
              method: 'POST',
              url: '${b.url("course-or-user-search!userAjax")}',
              postData:function(){
                return {
                  pageIndex:1,
                  pageSize:10,
                }
              }
            }
            , function(data) {
              var dataObj=eval("(" + data + ")");
              var items = {};
              jQuery.each(dataObj.courses, function(i, course) {
                items[course.code] = course.name + '(' + course.code + ')';
              });
              return items;
            });

  })
</script>
[@b.foot/]