package ${package.Entity};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

<#list table.fields as field>
    <#if field.propertyType = "BigDecimal">
import java.math.BigDecimal;
        <#break>
    </#if>
</#list>

/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@TableName("${table.name}")
@ApiModel("${table.comment}")
public class ${entity} implements Serializable {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
	/**
	 * ${field.comment}
	 */
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
	@TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
        <#elseif idType??>
	@TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
        <#elseif field.convert>
	@TableId("${field.annotationColumnName}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
	@TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
        <#else>
	@TableField(fill = FieldFill.${field.fill})
        </#if>
<#--    <#elseif field.convert>-->
<#--	@TableField("${field.annotationColumnName}")-->
<#--    </#if>-->
	<#elseif field.annotationColumnName != field.propertyName>
	@TableField("${field.annotationColumnName}")
    </#if>
<#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
	@Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
	@TableLogic
    </#if>
	@ApiModelProperty("${field.comment}")
	private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->
}