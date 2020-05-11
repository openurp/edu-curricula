[#ftl]
[@b.head/]
<script language="JavaScript" type="text/JavaScript" src="${base}/static/js/ajax-chosen.js"></script>
[@b.toolbar title="课程资料维护"]bar.addBack();[/@]
  [#assign sa][#if reviseTask.persisted]!update?id=${reviseTask.id}[#else]!save[/#if][/#assign]
    [@b.form action=sa theme="list" enctype="multipart/form-data"]
      [@b.field label="学年学期"]${reviseTask.semester.schoolYear}学年${reviseTask.semester.name}学期[/@]
      [@b.field label="课程"]${reviseTask.course.name}(${reviseTask.course.code})[/@]
      [@b.select name="courseBlog.meta.courseGroup.id" label="课程分组"]
        <option value="">...</option>
        [#list courseGroups as courseGroup]
          <option value="${courseGroup.id}" [#if meta.courseGroup == courseGroup]selected[/#if]>
            [#if (courseGroup.indexno?split('.'))?size == 2]&nbsp;&nbsp;
            [#elseif (courseGroup.indexno?split('.'))?size == 3]&nbsp;&nbsp;&nbsp;&nbsp;
            [/#if]
            ${courseGroup.name}
          </option>
        [/#list]
      [/@]
      [@b.textarea label="计划简介" name="courseBlog.description" value=(courseBlog.description)! cols="100" rows="10" required="true"/]
      [@b.textarea label="英文简介" name="courseBlog.enDescription" value=(courseBlog.enDescription)! cols="100" rows="10"/]
      [@b.textfield label="教材和辅助资料" name="courseBlog.materials" value=(courseBlog.materials)! style="width:250px"/]
      [@b.textfield label="课程网站" name="courseBlog.website" value=(courseBlog.website)! style="width:250px"/]
      [@b.field label="教学大纲附件" required="true"]
        <input name="syllabus.attachment" type="file" style="display:inline-block"/> <span style="color:red" >注：请上传pdf格式的文件</span>
        [#if courseBlog?? && syllabuses ?? && syllabuses?size>0]
          [#list syllabuses as syllabus]
            [#if syllabus.attachment??]
              <br>已有附件：${(syllabus.attachment.name)!}
              [@b.a target="_blank" href="syllabus!attachment?id=${syllabus.id}"]下载[/@]
              &nbsp;&nbsp;[@b.a target="_blank" href="syllabus!view?id=${syllabus.id}"]预览[/@]
              [#if syllabus_has_next]<br>[/#if]
            [/#if]
          [/#list]
        [/#if]
      [/@]
      [@b.field label="授课计划附件" required="true"]
        <input name="lecturePlan.attachment" type="file" style="display:inline-block"/> <span style="color:red" >注：请上传pdf格式的文件</span>
        [#if courseBlog?? && lecturePlans?? && lecturePlans?size>0 ]
          [#list lecturePlans as lecturePlan]
            [#if lecturePlan.attachment??]
              <br>已有附件：${(lecturePlan.attachment.name)!}
              [@b.a target="_blank" href="lecture-plan!attachment?id=${lecturePlan.id}"]下载[/@]
              &nbsp;&nbsp;[@b.a target="_blank" href="lecture-plan!view?id=${lecturePlan.id}"]预览[/@]
              [#if lecturePlan_has_next]<br>[/#if]
            [/#if]
          [/#list]
        [/#if]
      [/@]
      [@b.formfoot]
        [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
      [/@]
    [/@]

[@b.foot/]