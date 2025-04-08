package com.miaoyongzheng.models.scrpits

import java.io.File
import kotlin.random.Random

fun main() {
    generateHospitalTags()
}

fun generateHospitalTags() {
    // 标签池
    val tagsPool = listOf(
        "三甲", "三乙", "二甲", "二乙", "三级", "二级",
        "复旦TOP100", "中国医院排行榜", "国家重点专科",
        "区域医疗中心", "省级重点", "市级重点",
        "心血管病重点", "肿瘤学重点", "神经内科重点",
        "呼吸科重点", "消化内科优势", "肾脏病重点",
        "内分泌特色", "风湿免疫重点", "血液科优势",
        "传染病重点", "急诊医学重点", "重症医学重点",
        "老年医学重点", "全科医学重点", "儿科特色",
        "妇产专科", "眼科优势", "耳鼻喉特色",
        "胸外科领先", "神经外科特色", "骨科特色",
        "泌尿外科特色", "烧伤外科重点", "整形外科优势",
        "皮肤科重点", "中医特色", "康复医学特色",
        "疼痛科特色", "精神卫生特色", "睡眠医学特色",
        "核医学科重点", "病理科重点", "检验科特色",
        "影像科特色", "超声科重点", "麻醉科特色",
        "介入科优势", "输血科重点", "营养科特色",
        "体检中心特色", "健康管理特色"
    )

    val random = java.util.Random()
    val sqlStatements = StringBuilder("INSERT INTO hospital_tags (hospital_id, tag) VALUES\n")

    // 为459家医院生成标签
    for (hospitalId in 1..459) {
        val tagCount = random.nextInt(3) + 1 // 每家医院1-3个标签
        val selectedTags = tagsPool.shuffled().take(tagCount)

        selectedTags.forEach { tag ->
            sqlStatements.append("($hospitalId,'$tag'),\n")
        }
    }

    // 移除最后一个逗号和换行
    sqlStatements.delete(sqlStatements.length - 2, sqlStatements.length)
    sqlStatements.append(";")

    println(sqlStatements.toString())
    // 将SQL语句写入文件
    val file = File("hospital_tag_sql")
    file.writeText(sqlStatements.toString())

}