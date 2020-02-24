[#ftl]
[@b.head/]
<script language="JavaScript" type="text/JavaScript" src="${base}/static/js/ajax-chosen.js"></script>
[@b.toolbar title="上传教学大纲"]bar.addBack();[/@]
  [#assign sa][#if syllabus.persisted]!update?id=${syllabus.id}[#else]!save[/#if][/#assign]
    [@b.form action=sa theme="list"  enctype="multipart/form-data"]
      [@b.select name="syllabus.semester.id" label="学年学期" items=semesters  value=currentSemester option="id,code" required="true"/]
      [@b.field label="选择课程" required='true']
        <select id="courses" name="syllabus.course" style="width:200px;" >
          <option value="${(syllabus.course.code)!}" selected>${(syllabus.course.name)!}${(syllabus.course.code)!}</option>
        </select>
      [/@]
      [@b.select name="syllabus.locale" label="语言" items={} required="true"]
        [#if syllabus.persisted]
          <option value="zh" [#if syllabus.locale='zh']selected="selected"[/#if]>中文</option>
          <option value="en" [#if syllabus.locale='en']selected="selected"[/#if]>English</option>
        [#else]
          <option value="" selected="selected">...</option>
          <option value="zh">中文</option>
          <option value="en">English</option>
        [/#if]
      [/@]
      [@b.field label="教学大纲附件" required="true"]
        <input name="attachment" type="file" style="display:inline-block"/> <span style="color:red" >注：请上传pdf格式的文件</span>
        [#if syllabus.persisted]
          <br>已有附件：${(syllabus.attachment.name)!}
          [@b.a target="_blank" href="!attachment?id=${syllabus.id}"]下载[/@]
        [/#if]
      [/@]
      [@b.formfoot]
        [#if syllabus.persisted]<input type="hidden" name="syllabus.id" value="${syllabus.id!}" />[/#if]
        [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
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
</script>
[@b.foot/]