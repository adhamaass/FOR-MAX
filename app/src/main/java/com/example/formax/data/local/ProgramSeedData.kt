package com.example.formax.data.local

import com.example.formax.domain.models.*

object ProgramSeedData {

    fun getInitialPrograms(): List<TrainingProgramEntity> {
        return listOf(
            TrainingProgramEntity(
                id = "upper_lower_4day",
                name = "فور ماكس: علوي / سفلي (4 أيام)",
                systemType = SplitSystemType.UPPER_LOWER.name,
                description = "نظام متكامل لمدة 4 أيام يوازن بين التحفيز العضلي العالي وفترة استشفاء من 48 إلى 72 ساعة. يحقق حوالي 10 إلى 12 جولة فعلية لكل عضلة أسبوعياً.",
                goal = FitnessGoal.FAT_LOSS_AND_MUSCLE_GAIN.name,
                experienceLevel = TrainingExperience.INTERMEDIATE.name,
                weeklyDays = 4,
                isRecommended = true,
                recommendationReason = "توزيع جلسات الجزء العلوي والسفلي على مدار الأسبوع لتحقيق أفضل توازن بين حجم التمرين والاستشفاء.",
                status = ContentStatus.PUBLISHED.name
            ),
            TrainingProgramEntity(
                id = "full_body_3day",
                name = "فور ماكس: الجسم بالكامل (3 أيام)",
                systemType = SplitSystemType.FULL_BODY.name,
                description = "يتم تدريب كل المجموعات العضلية الرئيسية في كل جلسة خلال 3 أيام غير متتالية. مثالي لزيادة وتيرة تخليق البروتين.",
                goal = FitnessGoal.GENERAL_FITNESS.name,
                experienceLevel = TrainingExperience.BEGINNER.name,
                weeklyDays = 3,
                isRecommended = false,
                recommendationReason = "تدريب كل مجموعة عضلية رئيسية في نفس الجلسة، ممتاز لمن لديهم وقت تدريب أقل.",
                status = ContentStatus.PUBLISHED.name
            ),
            TrainingProgramEntity(
                id = "ppl_6day",
                name = "فور ماكس: دفع / سحب / أرجل (6 أيام)",
                systemType = SplitSystemType.PUSH_PULL_LEGS.name,
                description = "تقسيم عالي التردد منظم بسلاسل حركية: دفع (الصدر، الأكتاف، التراي)، سحب (الظهر، الباي)، وأرجل (الفخذ، السمانة).",
                goal = FitnessGoal.MUSCLE_GAIN.name,
                experienceLevel = TrainingExperience.ADVANCED.name,
                weeklyDays = 6,
                isRecommended = false,
                recommendationReason = "تنظيم الجلسات حسب سلاسل الحركة لتحقيق أقصى كثافة استهداف وتضخيم.",
                status = ContentStatus.PUBLISHED.name
            ),
            TrainingProgramEntity(
                id = "hybrid_5day",
                name = "فور ماكس: هجين 5 أيام (قوة وتضخيم)",
                systemType = SplitSystemType.FIVE_DAY_HYBRID.name,
                description = "يدمج بين تطوير القوة في التمارين المركبة الأساسية مع أحجام تضخيم موضعية عالية.",
                goal = FitnessGoal.STRENGTH.name,
                experienceLevel = TrainingExperience.ADVANCED.name,
                weeklyDays = 5,
                isRecommended = false,
                recommendationReason = "دمج تمارين القوة التراكمية مع جولات التضخيم العالي عبر 5 أيام تدريبية.",
                status = ContentStatus.PUBLISHED.name
            )
        )
    }

    fun getInitialDays(): List<TrainingDayEntity> {
        val days = mutableListOf<TrainingDayEntity>()

        // 1. Upper / Lower 4-Day
        days.add(
            TrainingDayEntity(
                id = "ul_day1",
                programId = "upper_lower_4day",
                dayNumber = 1,
                dayName = "الجزء العلوي (أ) - Upper A",
                focus = "الصدر، الظهر، الأكتاف والذراعين",
                isRestDay = false,
                isActiveRecovery = false,
                exercises = listOf(
                    ProgramExercise("bench_press", 1, targetSets = 3, minReps = 6, maxReps = 10, rirTarget = 2, restTimeSeconds = 180),
                    ProgramExercise("lat_pulldown", 2, targetSets = 3, minReps = 8, maxReps = 12, rirTarget = 2, restTimeSeconds = 120),
                    ProgramExercise("overhead_press", 3, targetSets = 3, minReps = 8, maxReps = 10, rirTarget = 2, restTimeSeconds = 120),
                    ProgramExercise("seated_cable_row", 4, targetSets = 3, minReps = 10, maxReps = 12, rirTarget = 1, restTimeSeconds = 90),
                    ProgramExercise("barbell_curl", 5, targetSets = 3, minReps = 10, maxReps = 12, rirTarget = 1, restTimeSeconds = 75),
                    ProgramExercise("cable_pushdown", 6, targetSets = 3, minReps = 10, maxReps = 14, rirTarget = 1, restTimeSeconds = 60)
                )
            )
        )

        days.add(
            TrainingDayEntity(
                id = "ul_day2",
                programId = "upper_lower_4day",
                dayNumber = 2,
                dayName = "الجزء السفلي (أ) - Lower A",
                focus = "الأفخاذ الأمامية، الأوتار الخلفية والسمانة",
                isRestDay = false,
                isActiveRecovery = false,
                exercises = listOf(
                    ProgramExercise("back_squat", 1, targetSets = 3, minReps = 6, maxReps = 10, rirTarget = 2, restTimeSeconds = 180),
                    ProgramExercise("romanian_deadlift", 2, targetSets = 3, minReps = 8, maxReps = 12, rirTarget = 2, restTimeSeconds = 150),
                    ProgramExercise("leg_press", 3, targetSets = 3, minReps = 10, maxReps = 14, rirTarget = 2, restTimeSeconds = 120),
                    ProgramExercise("seated_leg_curl", 4, targetSets = 3, minReps = 10, maxReps = 14, rirTarget = 1, restTimeSeconds = 90),
                    ProgramExercise("calf_raise", 5, targetSets = 4, minReps = 12, maxReps = 16, rirTarget = 1, restTimeSeconds = 60),
                    ProgramExercise("cable_crunch", 6, targetSets = 3, minReps = 12, maxReps = 15, rirTarget = 1, restTimeSeconds = 60)
                )
            )
        )

        days.add(
            TrainingDayEntity(
                id = "ul_day3",
                programId = "upper_lower_4day",
                dayNumber = 3,
                dayName = "راحة واستشفاء نشط",
                focus = "مرونة حركية، مشي خفيف وتغذية بروتينية",
                isRestDay = true,
                isActiveRecovery = true,
                exercises = emptyList()
            )
        )

        days.add(
            TrainingDayEntity(
                id = "ul_day4",
                programId = "upper_lower_4day",
                dayNumber = 4,
                dayName = "الجزء العلوي (ب) - Upper B",
                focus = "تضخيم الصدر العلوي والأكتاف الخلفية",
                isRestDay = false,
                isActiveRecovery = false,
                exercises = listOf(
                    ProgramExercise("incline_dumbbell_press", 1, targetSets = 3, minReps = 8, maxReps = 12, rirTarget = 2, restTimeSeconds = 120),
                    ProgramExercise("chest_supported_row", 2, targetSets = 3, minReps = 8, maxReps = 12, rirTarget = 2, restTimeSeconds = 120),
                    ProgramExercise("lateral_raise", 3, targetSets = 4, minReps = 12, maxReps = 15, rirTarget = 1, restTimeSeconds = 60),
                    ProgramExercise("cable_fly", 4, targetSets = 3, minReps = 12, maxReps = 15, rirTarget = 1, restTimeSeconds = 75),
                    ProgramExercise("hammer_curl", 5, targetSets = 3, minReps = 10, maxReps = 12, rirTarget = 1, restTimeSeconds = 75),
                    ProgramExercise("overhead_cable_extension", 6, targetSets = 3, minReps = 10, maxReps = 12, rirTarget = 1, restTimeSeconds = 75)
                )
            )
        )

        days.add(
            TrainingDayEntity(
                id = "ul_day5",
                programId = "upper_lower_4day",
                dayNumber = 5,
                dayName = "الجزء السفلي (ب) - Lower B",
                focus = "التركيز على عضلات المؤخرة والأوتار الخلفية",
                isRestDay = false,
                isActiveRecovery = false,
                exercises = listOf(
                    ProgramExercise("hip_thrust", 1, targetSets = 3, minReps = 8, maxReps = 12, rirTarget = 2, restTimeSeconds = 150),
                    ProgramExercise("hack_squat", 2, targetSets = 3, minReps = 8, maxReps = 12, rirTarget = 2, restTimeSeconds = 120),
                    ProgramExercise("leg_curl", 3, targetSets = 3, minReps = 10, maxReps = 14, rirTarget = 1, restTimeSeconds = 90),
                    ProgramExercise("leg_extension", 4, targetSets = 3, minReps = 12, maxReps = 15, rirTarget = 1, restTimeSeconds = 75),
                    ProgramExercise("seated_calf_raise", 5, targetSets = 3, minReps = 12, maxReps = 15, rirTarget = 1, restTimeSeconds = 60),
                    ProgramExercise("hanging_knee_raise", 6, targetSets = 3, minReps = 10, maxReps = 15, rirTarget = 1, restTimeSeconds = 60)
                )
            )
        )

        days.add(
            TrainingDayEntity(
                id = "ul_day6",
                programId = "upper_lower_4day",
                dayNumber = 6,
                dayName = "استشفاء نشط",
                focus = "كارديو زون 2 وتمارين إطالة",
                isRestDay = true,
                isActiveRecovery = true,
                exercises = emptyList()
            )
        )

        days.add(
            TrainingDayEntity(
                id = "ul_day7",
                programId = "upper_lower_4day",
                dayNumber = 7,
                dayName = "يوم راحة كامل",
                focus = "نوم عميق وإعادة بناء الأنسجة العضلية",
                isRestDay = true,
                isActiveRecovery = false,
                exercises = emptyList()
            )
        )

        return days
    }
}
