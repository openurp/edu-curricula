[#ftl]
{
  "courseGroups" : [
                [#list courseGroups! as courseGroup]{ "id" : "${courseGroup.id}", "name" : "${courseGroup.name?js_string}", "code" : "${courseGroup.code?js_string}"}[#if courseGroup_has_next],[/#if][/#list]
              ]
}
