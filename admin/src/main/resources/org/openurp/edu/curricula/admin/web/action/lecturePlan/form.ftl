[#ftl]
[@b.head/]
<script language="JavaScript" type="text/JavaScript" src="${base}/static/js/ajax-chosen.js"></script>
[@b.toolbar title="上传授课计划"]bar.addBack();[/@]
  [#assign sa][#if lecturePlan.persisted]!update?id=${lecturePlan.id}[#else]!save[/#if][/#assign]
    [@b.form action=sa theme="list"  enctype="multipart/form-data"]
      [@edu_base.semester name="lecturePlan.semester.id" label="学年学期"  value=currentSemester required="true"/]
      [@b.field label="选择课程" required='true']
        <select id="courses" name="lecturePlan.course" style="width:200px;" >
          <option value="${(lecturePlan.course.code)!}" selected>${(lecturePlan.course.name)!}${(lecturePlan.course.code)!}</option>
        </select>
      [/@]
      [@b.select name="lecturePlan.locale" label="语言" items={} required="true"]
        [#if lecturePlan.persisted]
          <option value="zh" [#if lecturePlan.locale='zh']selected="selected"[/#if]>中文</option>
          <option value="en" [#if lecturePlan.locale='en']selected="selected"[/#if]>English</option>
        [#else]
          <option value="" selected="selected">...</option>
          <option value="zh">中文</option>
          <option value="en">English</option>
        [/#if]
      [/@]
      [@b.field label="授课计划附件" required="true"]
        <input name="attachment" type="file" style="display:inline-block"/> <span style="color:red" >注：请上传pdf格式的文件</span>
        [#if lecturePlan.persisted]
          <br>已有附件：${(lecturePlan.attachment.name)!}
          [@b.a target="_blank" href="!attachment?id=${lecturePlan.id}"]下载[/@]
        [/#if]
      [/@]
      [@b.formfoot]
        [#if lecturePlan.persisted]<input type="hidden" name="lecturePlan.id" value="${lecturePlan.id!}" />[/#if]
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