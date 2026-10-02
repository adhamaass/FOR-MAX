package com.example.formax.domain.engine

import com.example.formax.domain.models.EquipmentAvailable
import com.example.formax.domain.models.FitnessGoal
import com.example.formax.domain.models.SplitSystemType
import com.example.formax.domain.models.TrainingExperience

data class SplitRecommendation(
    val recommendedSystem: SplitSystemType,
    val explanation: String,
    val recommendedWeeklyDays: Int,
    val weeklyVolumeSetsPerMuscle: Int = 10,
    val allSystemsWithContext: List<SystemContext>
)

data class SystemContext(
    val systemType: SplitSystemType,
    val isRecommended: Boolean,
    val idealDays: Int,
    val rationale: String
)

object TrainingEngine {

    /**
     * Deterministic recommendation engine in Arabic.
     * Never claims that one split is universally superior.
     */
    fun recommendTrainingSystem(
        experience: TrainingExperience,
        goal: FitnessGoal,
        targetDays: Int,
        equipment: EquipmentAvailable
    ): SplitRecommendation {
        val (recommended, explanation, optimalDays) = when {
            targetDays <= 3 || experience == TrainingExperience.BEGINNER -> {
                Triple(
                    SplitSystemType.FULL_BODY,
                    "يتم تدريب كل مجموعة عضلية رئيسية خلال نفس الجلسة. يمنحك ذلك وتيرة مثالية لتخليق البروتين العضلي (2-3 مرات أسبوعياً) مع ملاءمة جدول استشفاء لـ 3 أيام.",
                    3
                )
            }
            targetDays == 4 -> {
                Triple(
                    SplitSystemType.UPPER_LOWER,
                    "يتم توزيع جلسات الجزء العلوي والسفلي على مدار الأسبوع. يوفر هذا 48 إلى 72 ساعة من الاستشفاء بين الجلسات مع تحقيق حوالي 10 إلى 12 جولة فعلية لكل عضلة أسبوعياً.",
                    4
                )
            }
            targetDays == 5 && experience == TrainingExperience.ADVANCED -> {
                Triple(
                    SplitSystemType.FIVE_DAY_HYBRID,
                    "يدمج بين أساس القوة التراكمية في تمارين الجزء العلوي والسفلي المركبة، مع التركيز على تضخيم العضلات بنظام دفع/سحب/أرجل، وهو مثالي للاعبين المتقدمين.",
                    5
                )
            }
            targetDays >= 5 -> {
                Triple(
                    SplitSystemType.PUSH_PULL_LEGS,
                    "تُنظّم الجلسات وفق الميكانيكا الحركية وتوافق العضلات (دفع / سحب / أرجل). يتيح ذلك أقصى درجات التركيز الموضعي في كل جلسة مع فترات راحة كافية قبل تكرار السلسلة الحركية.",
                    if (targetDays >= 6) 6 else 5
                )
            }
            else -> {
                Triple(
                    SplitSystemType.UPPER_LOWER,
                    "نظام تدريبي مثبت ومثالي لتحقيق التوازن بين الزيادة التدريجية في الأحمال (Progressive Overload) والاستشفاء والبناء العضلي.",
                    4
                )
            }
        }

        val allContexts = SplitSystemType.values().map { type ->
            SystemContext(
                systemType = type,
                isRecommended = type == recommended,
                idealDays = when (type) {
                    SplitSystemType.FULL_BODY -> 3
                    SplitSystemType.UPPER_LOWER -> 4
                    SplitSystemType.PUSH_PULL_LEGS -> 6
                    SplitSystemType.FIVE_DAY_HYBRID -> 5
                    SplitSystemType.CUSTOM_ADVANCED -> targetDays
                },
                rationale = when (type) {
                    SplitSystemType.FULL_BODY ->
                        "تدريب كل مجموعة عضلية رئيسية في نفس الجلسة. ممتاز ومناسب عند التدريب أياماً أقل."
                    SplitSystemType.UPPER_LOWER ->
                        "توزيع جلسات الجزء العلوي والسفلي على مدار الأسبوع لتحقيق التوازن بين حجم التمرين والاستشفاء."
                    SplitSystemType.PUSH_PULL_LEGS ->
                        "تنظيم الجلسات حسب مسار الحركة والتركيز العضلي لضمان كثافة عالية في كل جلسة."
                    SplitSystemType.FIVE_DAY_HYBRID ->
                        "دمج دورات القوة والتضخيم عبر كتلة تدريبية مدتها 5 أيام في الأسبوع."
                    SplitSystemType.CUSTOM_ADVANCED ->
                        "اختيار مخصص بالكامل للتمارين وتوزيع الجولات لتحقيق أهداف رياضية محددة."
                }
            )
        }

        return SplitRecommendation(
            recommendedSystem = recommended,
            explanation = explanation,
            recommendedWeeklyDays = optimalDays,
            weeklyVolumeSetsPerMuscle = 10,
            allSystemsWithContext = allContexts
        )
    }

    fun calculateWeeklyTargetSetsPerMuscle(experience: TrainingExperience): Int {
        return when (experience) {
            TrainingExperience.BEGINNER -> 8
            TrainingExperience.INTERMEDIATE -> 10
            TrainingExperience.ADVANCED -> 14
        }
    }
}
