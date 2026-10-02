package com.example.formax.data.local

import com.example.formax.domain.models.*

object ExerciseSeedData {

    fun getInitialExercises(): List<ExerciseEntity> {
        val list = mutableListOf<ExerciseEntity>()

        // 1. CHEST
        list.add(createExercise(
            id = "bench_press",
            name = "Barbell Bench Press",
            altNames = listOf("Flat Bench Press", "BB Bench"),
            primary = MuscleGroup.CHEST,
            secondary = listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            movement = MovementPattern.HORIZONTAL_PUSH,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 6, maxReps = 10, rir = 2, restSec = 180,
            instructions = "Lie flat on the bench with feet firmly planted. Retract shoulder blades, unrack bar over chest, lower under control to mid-chest, and press upward with power.",
            setup = "Set bench height so eyes align directly under the racked bar. Plant feet flat, grip bar slightly wider than shoulder width, arch upper back and pinch scapulae.",
            execution = "Unrack with straight arms. Inhale and lower bar under control to the lower sternum. Touch chest gently without bouncing. Drive feet into floor and press bar up and slightly back toward eye line.",
            breathing = "Inhale deep belly breath and brace core before descent. Exhale through sticking point on the press.",
            mistakes = listOf("Excessive bouncing off sternum", "Flaring elbows 90 degrees out", "Lifting hips/glutes off the bench", "Failing to retract scapulae"),
            safety = "Always use safety pins or spotter for heavy sets. Keep wrists neutral without cocking backward.",
            cues = "Bend the bar in half, tuck elbows ~45 degrees, push the floor away with your legs.",
            progression = "When you hit 3 sets of 10 reps with 2 RIR, add 2.5 kg next session.",
            alternatives = listOf("dumbbell_bench_press", "machine_chest_press", "push_up")
        ))

        list.add(createExercise(
            id = "incline_bench_press",
            name = "Incline Barbell Bench Press",
            altNames = listOf("Incline Press"),
            primary = MuscleGroup.CHEST,
            secondary = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            movement = MovementPattern.HORIZONTAL_PUSH,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 150,
            instructions = "Set bench to 30 degrees. Unrack bar, lower under control to upper chest, and press to lockout emphasizing upper clavicular pectorals.",
            setup = "Bench angle at 30 degrees (avoid higher than 45 to minimize anterior delt dominance). Pinch scapulae firmly.",
            execution = "Lower bar steadily to upper chest just below collarbones. Press upward along an arc directly over shoulder joints.",
            breathing = "Inhale and brace before eccentric, exhale forcefully through concentric.",
            mistakes = listOf("Setting bench angle too steep (>45 degrees)", "Flaring elbows", "Bouncing bar on clavicles"),
            safety = "Use thumb-around grip for bar security.",
            cues = "Drive elbows inward, maintain chest high throughout.",
            progression = "Add 1.25–2.5 kg when upper rep target is achieved.",
            alternatives = listOf("incline_dumbbell_press", "machine_chest_press")
        ))

        list.add(createExercise(
            id = "dumbbell_bench_press",
            name = "Dumbbell Bench Press",
            altNames = listOf("DB Bench"),
            primary = MuscleGroup.CHEST,
            secondary = listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            movement = MovementPattern.HORIZONTAL_PUSH,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 120,
            instructions = "Sit on bench with dumbbells resting on thighs. Kick back onto bench, press dumbbells up in a slight convergent arc, lower with deep stretch.",
            setup = "Retract scapulae, plant feet firmly, dumbbells turned at slight 45-degree angle.",
            execution = "Lower dumbbells slowly to outer chest until a full pectoral stretch is felt. Press upward squeezing chest at top without banging weights together.",
            breathing = "Deep breath on descent, controlled exhale pressing up.",
            mistakes = listOf("Dropping dumbbells too fast", "Clanging weights at top", "Shrugging shoulders into ears"),
            safety = "If failing, lower weights to hips or drop carefully to floor with control.",
            cues = "Pull chest open on descent, squeeze inner chest at top.",
            progression = "Advance to next dumbbell pair (+2 kg/hand) when hitting 12 reps on all sets.",
            alternatives = listOf("bench_press", "machine_chest_press", "push_up")
        ))

        list.add(createExercise(
            id = "incline_dumbbell_press",
            name = "Incline Dumbbell Press",
            altNames = listOf("Incline DB Press"),
            primary = MuscleGroup.CHEST,
            secondary = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            movement = MovementPattern.HORIZONTAL_PUSH,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 120,
            instructions = "Set adjustable bench to 30 degrees. Kick dumbbells up to shoulder level. Press dumbbells upward over upper chest with full range of motion.",
            setup = "Bench at 30-degree incline. Firm foot base, upper back packed tightly against backrest.",
            execution = "Lower dumbbells with controlled 2-3 second cadence to upper chest. Press up smoothly.",
            breathing = "Inhale down, exhale up.",
            mistakes = listOf("Incline set to 60 degrees", "Cutting bottom stretch short", "Flaring elbows sideways"),
            safety = "Keep wrists stacked directly over elbows.",
            cues = "Elbows tucked 45 degrees, lead with upper chest.",
            progression = "Increase dumbbell weight once all sets reach 12 reps.",
            alternatives = listOf("incline_bench_press", "machine_chest_press")
        ))

        list.add(createExercise(
            id = "machine_chest_press",
            name = "Machine Chest Press",
            altNames = listOf("Seated Chest Press"),
            primary = MuscleGroup.CHEST,
            secondary = listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            movement = MovementPattern.HORIZONTAL_PUSH,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 10, maxReps = 14, rir = 1, restSec = 90,
            instructions = "Adjust seat height so handles align with mid-chest. Sit back, press handles forward to near-lockout, lower slowly under continuous tension.",
            setup = "Seat adjusted so handles sit at nipple line. Head and spine neutral against pad.",
            execution = "Drive handles forward without locking elbows rigidly. Control return until chest is stretched.",
            breathing = "Exhale pushing forward, inhale on release.",
            mistakes = listOf("Setting seat too low putting strain on shoulders", "Releasing tension at bottom"),
            safety = "Use foot pedal to assist unrack and avoid shoulder impingement.",
            cues = "Pinch shoulder blades into pad, drive elbows toward each other.",
            progression = "Increment pin weight by 2.5-5 kg upon hitting 14 reps.",
            alternatives = listOf("bench_press", "dumbbell_bench_press")
        ))

        list.add(createExercise(
            id = "cable_fly",
            name = "Cable Chest Fly",
            altNames = listOf("Cable Crossover", "High to Low Cable Fly"),
            primary = MuscleGroup.CHEST,
            secondary = listOf(MuscleGroup.SHOULDERS),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 75,
            instructions = "Stand centered between cable pulleys with handles. Step forward into staggered stance, bring hands together in a wide hugging arc.",
            setup = "Pulleys set at chest height. Keep slight bend in elbows that remains fixed throughout.",
            execution = "Bring handles together in front of sternum, squeezing pecs for 1 second. Open arms wide until deep chest stretch is reached.",
            breathing = "Inhale expanding chest, exhale bringing hands together.",
            mistakes = listOf("Bending and straightening arms turning it into a press", "Excessive forward torso momentum"),
            safety = "Maintain controlled speed on eccentric to protect shoulder capsule.",
            cues = "Hug a giant tree trunk, contract inner pecs.",
            progression = "Focus on 1-second peak squeeze before increasing pin load.",
            alternatives = listOf("pec_deck", "dumbbell_bench_press")
        ))

        list.add(createExercise(
            id = "pec_deck",
            name = "Pec Deck Machine",
            altNames = listOf("Machine Fly", "Seated Pec Fly"),
            primary = MuscleGroup.CHEST,
            secondary = listOf(MuscleGroup.SHOULDERS),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 75,
            instructions = "Sit with back flat against pad. Place forearms/hands on pads. Squeeze pads together in front of chest, hold peak contraction, return slowly.",
            setup = "Seat height positioned so elbows align with mid-chest level.",
            execution = "Drive pads together with chest contraction. Pause at full contraction, return with 3-second stretch.",
            breathing = "Exhale on contraction, inhale on opening.",
            mistakes = listOf("Letting weight slam between reps", "Shoulders rounding forward"),
            safety = "Do not hyper-extend shoulders excessively at back of stroke.",
            cues = "Keep chest puffed out, think about touching biceps together.",
            progression = "Add 1 plate once 15 clean reps are reached with 1-sec hold.",
            alternatives = listOf("cable_fly")
        ))

        list.add(createExercise(
            id = "push_up",
            name = "Push-Up",
            altNames = listOf("Standard Push-Up", "Floor Press"),
            primary = MuscleGroup.CHEST,
            secondary = listOf(MuscleGroup.TRICEPS, MuscleGroup.CORE, MuscleGroup.SHOULDERS),
            movement = MovementPattern.HORIZONTAL_PUSH,
            equipment = EquipmentAvailable.BODYWEIGHT_HOME,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.BODYWEIGHT,
            sets = 3, minReps = 12, maxReps = 20, rir = 1, restSec = 60,
            instructions = "Support body in rigid plank with hands shoulder-width apart. Lower chest to 1 inch above floor, press back to full extension.",
            setup = "Hands under shoulders, glutes clenched, core braced like a plank.",
            execution = "Lower torso in one straight line until chest nearly touches floor. Press through palms.",
            breathing = "Inhale down, exhale driving floor away.",
            mistakes = listOf("Sagging lower back", "Worming hips up first", "Flaring elbows perpendicular to body"),
            safety = "Maintain neutral cervical spine; do not crane neck down.",
            cues = "Rigid plank, screw hands into the floor.",
            progression = "Elevate feet on bench or add weight vest for progression.",
            alternatives = listOf("bench_press", "machine_chest_press")
        ))

        // 2. BACK
        list.add(createExercise(
            id = "lat_pulldown",
            name = "Lat Pulldown",
            altNames = listOf("Cable Pulldown"),
            primary = MuscleGroup.BACK,
            secondary = listOf(MuscleGroup.BICEPS, MuscleGroup.SHOULDERS),
            movement = MovementPattern.VERTICAL_PULL,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 90,
            instructions = "Grip wide bar just outside shoulders. Sit with thighs locked under pads. Pull bar down toward upper collarbones, driving elbows down and back.",
            setup = "Lock knee pads securely over thighs so body does not elevate.",
            execution = "Lean torso back ~10-15 degrees. Pull bar down smoothly to upper chest, squeeze lats, extend arms up for full stretch.",
            breathing = "Exhale pulling down, inhale on controlled release.",
            mistakes = listOf("Excessive swinging backward momentum", "Pulling bar behind neck", "Rounding upper spine"),
            safety = "Never pull bar behind neck; always pull to upper chest.",
            cues = "Drive elbows down into your back pockets.",
            progression = "Add pin weight once 12 clean reps with zero momentum are completed.",
            alternatives = listOf("pull_up", "assisted_pull_up")
        ))

        list.add(createExercise(
            id = "pull_up",
            name = "Pull-Up",
            altNames = listOf("Overhand Pull-Up"),
            primary = MuscleGroup.BACK,
            secondary = listOf(MuscleGroup.BICEPS, MuscleGroup.CORE),
            movement = MovementPattern.VERTICAL_PULL,
            equipment = EquipmentAvailable.BODYWEIGHT_HOME,
            difficulty = Difficulty.ADVANCED,
            type = ExerciseType.BODYWEIGHT,
            sets = 3, minReps = 6, maxReps = 10, rir = 1, restSec = 120,
            instructions = "Hang from bar with pronated grip slightly wider than shoulders. Depress scapulae, pull chest up toward bar until chin clears bar, lower to full dead-hang.",
            setup = "Dead hang with arms fully extended, core and legs braced.",
            execution = "Depress shoulder blades first, then pull through elbows until upper chest reaches bar level. Lower under control.",
            breathing = "Exhale pulling up, inhale lowering.",
            mistakes = listOf("Kipping with knees/hips", "Half reps without full extension", "Shrugging shoulders at top"),
            safety = "Do not disengage shoulder girdle violently at the bottom.",
            cues = "Pull your chest to the bar, not your chin.",
            progression = "Add dip belt with weight plates once 10 bodyweight reps are mastered.",
            alternatives = listOf("assisted_pull_up", "lat_pulldown")
        ))

        list.add(createExercise(
            id = "assisted_pull_up",
            name = "Assisted Pull-Up",
            altNames = listOf("Band/Machine Pull-Up"),
            primary = MuscleGroup.BACK,
            secondary = listOf(MuscleGroup.BICEPS),
            movement = MovementPattern.VERTICAL_PULL,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 90,
            instructions = "Kneel or stand on counterweight assist platform. Grasp pull-up handles, pull body up smoothly to chin height, lower to full extension.",
            setup = "Set counterweight appropriately (higher weight = easier).",
            execution = "Keep torso upright and core engaged. Pull through lats.",
            breathing = "Exhale on pull, inhale lowering.",
            mistakes = listOf("Dropping onto platform abruptly", "Using too little or too much assist"),
            safety = "Step off machine carefully with both hands holding frame.",
            cues = "Lead with chest, feel lats engage from full stretch.",
            progression = "Reduce assistance weight by 5 kg as strength improves.",
            alternatives = listOf("lat_pulldown", "pull_up")
        ))

        list.add(createExercise(
            id = "seated_cable_row",
            name = "Seated Cable Row",
            altNames = listOf("Cable Low Row"),
            primary = MuscleGroup.BACK,
            secondary = listOf(MuscleGroup.BICEPS, MuscleGroup.SHOULDERS),
            movement = MovementPattern.HORIZONTAL_PULL,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 10, maxReps = 12, rir = 2, restSec = 90,
            instructions = "Sit with feet on footplates, knees slightly bent. Grasp V-handle. Pull handle to lower abdomen while pinching shoulder blades together.",
            setup = "Spine neutral, slight bend in knees, chest high.",
            execution = "Pull handle toward navel. Squeeze mid-back rhomboids for 1 second. Extend arms with controlled stretch without rounding spine.",
            breathing = "Exhale on pull, inhale on return.",
            mistakes = listOf("Rounding lower back", "Leaning back excessively to jerk weight"),
            safety = "Never round lumbar spine under load.",
            cues = "Pinch a tennis ball between shoulder blades, pull with elbows.",
            progression = "Increase weight plate when 12 reps with strict pause are achieved.",
            alternatives = listOf("chest_supported_row", "barbell_row", "dumbbell_row")
        ))

        list.add(createExercise(
            id = "chest_supported_row",
            name = "Chest Supported Row",
            altNames = listOf("Incline DB Row", "T-Bar Row with Support"),
            primary = MuscleGroup.BACK,
            secondary = listOf(MuscleGroup.BICEPS, MuscleGroup.SHOULDERS),
            movement = MovementPattern.HORIZONTAL_PULL,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 90,
            instructions = "Lie face down on incline bench with dumbbells or use machine pad. Row handles toward ribcage without letting chest leave pad.",
            setup = "Pad resting right below clavicles, feet anchored firmly.",
            execution = "Row elbows up and back, squeezing lats and rhomboids. Lower with 2-second eccentric.",
            breathing = "Exhale pulling, inhale lowering.",
            mistakes = listOf("Cheating by hyperextending neck and lifting chest off pad"),
            safety = "Safe on lower back due to full chest support.",
            cues = "Drive elbows toward ceiling, hold squeeze at top.",
            progression = "Add 2.5 kg once hitting 12 reps on all sets.",
            alternatives = listOf("seated_cable_row", "dumbbell_row")
        ))

        list.add(createExercise(
            id = "barbell_row",
            name = "Barbell Bent-Over Row",
            altNames = listOf("Bent Over Row", "Pendlay Row"),
            primary = MuscleGroup.BACK,
            secondary = listOf(MuscleGroup.BICEPS, MuscleGroup.CORE, MuscleGroup.LEGS),
            movement = MovementPattern.HORIZONTAL_PULL,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.ADVANCED,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 6, maxReps = 10, rir = 2, restSec = 150,
            instructions = "Hinge hips back with torso around 45 degrees, flat back. Grip bar outside knees. Pull bar to upper abdomen, lower with control.",
            setup = "Hip hinge position, knees soft, spine rigid, bar directly over midfoot.",
            execution = "Pull bar toward navel driving through elbows. Squeeze lats without rising out of hinge.",
            breathing = "Brace core tight, exhale on pull, inhale on descent.",
            mistakes = listOf("Rounding lower back", "Standing upright using hip extension"),
            safety = "Keep spine neutral to protect lumbar spine.",
            cues = "Pull with your elbows, stay hinged over the bar.",
            progression = "Add 2.5 kg when 10 strict reps are completed across all sets.",
            alternatives = listOf("chest_supported_row", "dumbbell_row", "seated_cable_row")
        ))

        list.add(createExercise(
            id = "dumbbell_row",
            name = "Single-Arm Dumbbell Row",
            altNames = listOf("One-Arm DB Row"),
            primary = MuscleGroup.BACK,
            secondary = listOf(MuscleGroup.BICEPS, MuscleGroup.CORE),
            movement = MovementPattern.HORIZONTAL_PULL,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 90,
            instructions = "Place one knee and hand on flat bench. With free arm, hold dumbbell, pull elbow toward hip, lower to full lat stretch.",
            setup = "Back flat parallel to bench, supporting hand directly under shoulder.",
            execution = "Row dumbbell along arc toward hip. Avoid twisting torso.",
            breathing = "Exhale pulling, inhale lowering.",
            mistakes = listOf("Rotating torso excessively", "Pulling dumbbell up to shoulder rather than hip"),
            safety = "Keep neck neutral; do not look up at mirror.",
            cues = "Pull your elbow to your hip pocket.",
            progression = "Advance dumbbell weight when hitting 12 reps per arm.",
            alternatives = listOf("seated_cable_row", "chest_supported_row")
        ))

        list.add(createExercise(
            id = "machine_row",
            name = "Machine Row",
            altNames = listOf("Plate Loaded Row"),
            primary = MuscleGroup.BACK,
            secondary = listOf(MuscleGroup.BICEPS),
            movement = MovementPattern.HORIZONTAL_PULL,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 10, maxReps = 12, rir = 1, restSec = 90,
            instructions = "Adjust chest pad so arms reach handles with full stretch. Pull handles back, contracting lats and mid-back.",
            setup = "Chest against pad, feet planted on floor.",
            execution = "Pull handles backward, squeeze scapulae together, return with slow cadence.",
            breathing = "Exhale pulling, inhale releasing.",
            mistakes = listOf("Letting weight stack crash", "Rocking torso"),
            safety = "Use chest pad for lumbar stabilization.",
            cues = "Initiate movement by retracting shoulder blades.",
            progression = "Add 2.5–5 kg upon reaching target reps.",
            alternatives = listOf("seated_cable_row", "chest_supported_row")
        ))

        // 3. SHOULDERS
        list.add(createExercise(
            id = "overhead_press",
            name = "Barbell Overhead Press",
            altNames = listOf("OHP", "Military Press", "Standing Shoulder Press"),
            primary = MuscleGroup.SHOULDERS,
            secondary = listOf(MuscleGroup.TRICEPS, MuscleGroup.CORE),
            movement = MovementPattern.VERTICAL_PUSH,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 6, maxReps = 10, rir = 2, restSec = 150,
            instructions = "Hold bar at collarbone height with elbows slightly in front of bar. Press overhead in vertical line to lockout, clearing head.",
            setup = "Grip just outside shoulders, forearms vertical, glutes and abs locked tight.",
            execution = "Pull chin slightly back to let bar pass. Once bar clears forehead, push head forward to neutral and lock out bar over mid-foot.",
            breathing = "Big breath and core brace before press, exhale near top.",
            mistakes = listOf("Excessive lumbar hyperextension (leaning back)", "Pressing bar forward rather than overhead"),
            safety = "Clench glutes hard to protect lower spine.",
            cues = "Punch ceiling, head through the window at top.",
            progression = "Add 1.25 kg microplates when 10 reps are achieved.",
            alternatives = listOf("dumbbell_shoulder_press", "machine_shoulder_press")
        ))

        list.add(createExercise(
            id = "dumbbell_shoulder_press",
            name = "Seated Dumbbell Shoulder Press",
            altNames = listOf("Seated DB Press"),
            primary = MuscleGroup.SHOULDERS,
            secondary = listOf(MuscleGroup.TRICEPS),
            movement = MovementPattern.VERTICAL_PUSH,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 120,
            instructions = "Sit on upright bench. Press dumbbells from shoulder height up overhead until arms are nearly straight.",
            setup = "Bench at 85 degrees (not completely vertical). Dumbbells brought to shoulder level with slight tuck in elbows.",
            execution = "Press weights smoothly up along converging arc. Lower with control to ear level.",
            breathing = "Exhale pushing upward, inhale lowering.",
            mistakes = listOf("Flaring elbows 90 degrees outward", "Banging dumbbells at top"),
            safety = "Do not drop dumbbells abruptly; bring down to knees first.",
            cues = "Elbows in the scapular plane (~30 deg forward), press up and slightly inward.",
            progression = "Advance to next dumbbell size when hitting 12 reps on all sets.",
            alternatives = listOf("overhead_press", "machine_shoulder_press")
        ))

        list.add(createExercise(
            id = "machine_shoulder_press",
            name = "Machine Shoulder Press",
            altNames = listOf("Seated Machine Overhead Press"),
            primary = MuscleGroup.SHOULDERS,
            secondary = listOf(MuscleGroup.TRICEPS),
            movement = MovementPattern.VERTICAL_PUSH,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 10, maxReps = 12, rir = 1, restSec = 90,
            instructions = "Sit firmly against pad with handles at shoulder height. Press upward smoothly to lockout.",
            setup = "Seat height so handles start at chin/shoulder height.",
            execution = "Press handles overhead, pause briefly, control descent.",
            breathing = "Exhale up, inhale down.",
            mistakes = listOf("Arching back away from pad"),
            safety = "Safe guided path reduces stabilizer fatigue.",
            cues = "Keep back glued to backrest, drive through shoulders.",
            progression = "Add pin weight as target reps are achieved.",
            alternatives = listOf("dumbbell_shoulder_press", "overhead_press")
        ))

        list.add(createExercise(
            id = "lateral_raise",
            name = "Dumbbell Lateral Raise",
            altNames = listOf("Side Raise"),
            primary = MuscleGroup.SHOULDERS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.ISOLATION,
            sets = 4, minReps = 12, maxReps = 15, rir = 1, restSec = 60,
            instructions = "Stand holding dumbbells at sides. Raise arms out to sides until parallel to floor, leading with elbows.",
            setup = "Slight torso lean forward (~10 degrees), slight bend in elbows.",
            execution = "Raise dumbbells in scapular plane to shoulder level. Lower with 2-second tempo.",
            breathing = "Exhale lifting, inhale lowering.",
            mistakes = listOf("Using heavy swinging body momentum", "Shrugging traps up to ears", "Lifting above shoulder height"),
            safety = "Keep weight light to isolate lateral deltoid.",
            cues = "Pour water out of pitchers, push dumbbells out toward walls.",
            progression = "Focus on strict 15 reps before increasing weight.",
            alternatives = listOf("cable_lateral_raise")
        ))

        list.add(createExercise(
            id = "cable_lateral_raise",
            name = "Cable Lateral Raise",
            altNames = listOf("Single Arm Cable Side Raise"),
            primary = MuscleGroup.SHOULDERS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 60,
            instructions = "Set cable to lowest setting. Stand perpendicular, reach across and raise handle out to shoulder level with constant tension.",
            setup = "Pulley at ankle or knee level. Grip handle with opposite hand.",
            execution = "Raise cable arm outward to parallel with floor. Lower slowly maintaining constant tension on side delt.",
            breathing = "Exhale up, inhale down.",
            mistakes = listOf("Jerking weight stack from the bottom", "Leaning excessively"),
            safety = "Continuous cable tension eliminates bottom dead-zone.",
            cues = "Lead with the elbow, sweep hand wide.",
            progression = "Add 1.25 kg plate once 15 clean reps are logged.",
            alternatives = listOf("lateral_raise")
        ))

        list.add(createExercise(
            id = "rear_delt_fly",
            name = "Reverse Pec Deck Fly",
            altNames = listOf("Rear Delt Machine Fly"),
            primary = MuscleGroup.SHOULDERS,
            secondary = listOf(MuscleGroup.BACK),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 60,
            instructions = "Sit facing machine pad. Grip handles with pronated or neutral grip. Open arms out wide to sides contracting rear delts.",
            setup = "Chest supported against pad, seat adjusted so arms are parallel to floor.",
            execution = "Sweep handles outward and back until aligned with shoulders. Squeeze posterior delts.",
            breathing = "Exhale opening arms, inhale returning.",
            mistakes = listOf("Bending elbows turning it into a row", "Using mid-traps instead of rear delts"),
            safety = "Keep shoulder blades relatively fixed to focus on rear delts.",
            cues = "Push arms out wide to sides, feel tension behind shoulders.",
            progression = "Increase weight after achieving 15 reps with strict tempo.",
            alternatives = listOf("face_pull")
        ))

        list.add(createExercise(
            id = "face_pull",
            name = "Cable Face Pull",
            altNames = listOf("Rope Face Pull"),
            primary = MuscleGroup.SHOULDERS,
            secondary = listOf(MuscleGroup.BACK),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 60,
            instructions = "Attach rope to cable set at eye level. Grip with thumbs backward. Pull rope toward face while externally rotating hands back.",
            setup = "Step back into staggered stance for balance.",
            execution = "Pull rope toward bridge of nose, pulling rope ends apart and rotating fists backward. Hold peak squeeze for 1 second.",
            breathing = "Exhale pulling, inhale returning.",
            mistakes = listOf("Dropping elbows down", "Not externally rotating shoulders at finish"),
            safety = "Excellent for rotator cuff and postural health.",
            cues = "High elbows, double-bicep pose at finish.",
            progression = "Prioritize clean external rotation over heavy weight.",
            alternatives = listOf("rear_delt_fly")
        ))

        // 4. BICEPS
        list.add(createExercise(
            id = "barbell_curl",
            name = "Barbell Bicep Curl",
            altNames = listOf("BB Curl", "EZ Bar Curl"),
            primary = MuscleGroup.BICEPS,
            secondary = listOf(MuscleGroup.CORE),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.ISOLATION,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 90,
            instructions = "Stand holding barbell with underhand shoulder-width grip. Keep elbows pinned at sides, curl bar up to chest, lower under control.",
            setup = "Feet hip-width, core tight, elbows glued to ribcage.",
            execution = "Curl weight up contracting biceps. Pause at top, lower slowly with 3-second negative.",
            breathing = "Exhale curling up, inhale lowering down.",
            mistakes = listOf("Swinging hips and lumbar spine", "Allowing elbows to drift far forward"),
            safety = "Use EZ-curl bar if straight bar creates wrist discomfort.",
            cues = "Pin elbows to side ribs, squeeze biceps hard at top.",
            progression = "Add 1.25-2.5 kg when 12 reps are achieved without body sway.",
            alternatives = listOf("dumbbell_curl", "cable_curl")
        ))

        list.add(createExercise(
            id = "dumbbell_curl",
            name = "Standing Dumbbell Curl",
            altNames = listOf("Alternating DB Curl"),
            primary = MuscleGroup.BICEPS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.ISOLATION,
            sets = 3, minReps = 10, maxReps = 12, rir = 1, restSec = 75,
            instructions = "Hold dumbbells with neutral grip at sides. Curl upward, supinating (rotating palm up) through mid-range.",
            setup = "Stand upright, chest proud, dumbbells at sides.",
            execution = "Curl one or both dumbbells while turning palms toward ceiling. Squeeze at top, lower with control.",
            breathing = "Exhale on curl, inhale on descent.",
            mistakes = listOf("Swinging back and forth", "Not fully supinating palms"),
            safety = "Avoid hyperextending elbows at the bottom.",
            cues = "Turn pinky finger toward shoulder at top for peak contraction.",
            progression = "Advance to next dumbbell pair when 12 reps per arm are achieved.",
            alternatives = listOf("barbell_curl", "hammer_curl")
        ))

        list.add(createExercise(
            id = "hammer_curl",
            name = "Dumbbell Hammer Curl",
            altNames = listOf("Neutral Grip Curl"),
            primary = MuscleGroup.BICEPS,
            secondary = listOf(MuscleGroup.CORE),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.ISOLATION,
            sets = 3, minReps = 10, maxReps = 12, rir = 1, restSec = 75,
            instructions = "Hold dumbbells with palms facing each other (neutral grip). Curl weights up keeping thumbs pointing upward, targeting brachialis.",
            setup = "Stand upright with elbows at sides.",
            execution = "Curl dumbbells maintaining neutral grip throughout range. Lower slowly.",
            breathing = "Exhale up, inhale down.",
            mistakes = listOf("Rotating wrists during curl", "Using momentum"),
            safety = "Gentle on wrists; builds forearm and brachialis thickness.",
            cues = "Keep thumbs up like hammering a nail.",
            progression = "Increase weight after hitting 12 reps with zero swing.",
            alternatives = listOf("dumbbell_curl", "cable_curl")
        ))

        list.add(createExercise(
            id = "cable_curl",
            name = "Cable Bicep Curl",
            altNames = listOf("Straight Bar Cable Curl"),
            primary = MuscleGroup.BICEPS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 10, maxReps = 14, rir = 1, restSec = 60,
            instructions = "Attach straight or EZ bar to low pulley. Stand facing machine, curl bar toward shoulders with constant cable tension.",
            setup = "Step back half a step so cable has tension at full extension.",
            execution = "Curl bar up, squeeze at top, lower with slow 2-second cadence.",
            breathing = "Exhale curling, inhale lowering.",
            mistakes = listOf("Leaning back", "Elbows flaring"),
            safety = "Constant resistance curve maximizes hypertrophy stimulus.",
            cues = "Keep elbows locked in space, don't let weight stack rest.",
            progression = "Add 2.5 kg pin upon reaching 14 reps.",
            alternatives = listOf("barbell_curl", "preacher_curl")
        ))

        list.add(createExercise(
            id = "preacher_curl",
            name = "Preacher Curl",
            altNames = listOf("Scott Curl", "Machine Preacher Curl"),
            primary = MuscleGroup.BICEPS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.ISOLATION,
            sets = 3, minReps = 10, maxReps = 12, rir = 2, restSec = 75,
            instructions = "Sit with armpits snug against preacher pad, triceps flush with pad. Curl weight up, stop just before vertical, lower to controlled stretch.",
            setup = "Adjust seat so chest fits against edge and triceps lay flat on angled pad.",
            execution = "Curl bar smoothly up. Do not rest at the top. Lower with control to ~95% extension.",
            breathing = "Exhale up, inhale down.",
            mistakes = listOf("Hyperextending elbows abruptly at bottom", "Lifting body off seat"),
            safety = "Never bounce or rapidly snap arms into full lockout at bottom.",
            cues = "Keep upper arms glued to pad, squeeze inner bicep.",
            progression = "Add 1.25 kg when 12 reps are completed strictly.",
            alternatives = listOf("barbell_curl", "dumbbell_curl")
        ))

        // 5. TRICEPS
        list.add(createExercise(
            id = "cable_pushdown",
            name = "Tricep Cable Pushdown",
            altNames = listOf("Rope Pushdown", "Tricep Pressdown"),
            primary = MuscleGroup.TRICEPS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 10, maxReps = 14, rir = 1, restSec = 60,
            instructions = "Stand facing high pulley with rope or bar. Keep elbows tight against torso, push attachment down to full lockout, spreading rope at bottom.",
            setup = "Slight forward hip hinge, elbows tucked against sides.",
            execution = "Extend triceps to press down until arms are straight. Squeeze triceps for 1 second. Allow forearms to rise back to 90 degrees.",
            breathing = "Exhale pushing down, inhale returning.",
            mistakes = listOf("Letting elbows flare forward and back", "Using shoulder momentum"),
            safety = "Keep wrists firm and locked in line with forearms.",
            cues = "Elbows are hinges bolted to your ribs. Lock out at bottom.",
            progression = "Add pin weight when 14 clean reps with lockouts are hit.",
            alternatives = listOf("overhead_cable_extension", "skull_crusher")
        ))

        list.add(createExercise(
            id = "overhead_cable_extension",
            name = "Overhead Cable Tricep Extension",
            altNames = listOf("Cable French Press"),
            primary = MuscleGroup.TRICEPS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 10, maxReps = 12, rir = 1, restSec = 75,
            instructions = "Set pulley to chest height. Face away from cable holding rope overhead. Extend arms forward and up to full extension for long-head tricep focus.",
            setup = "Staggered stance, core braced, arms overhead with elbows pointing forward.",
            execution = "Extend elbows forward and out. Lower rope behind head under tension.",
            breathing = "Exhale extending, inhale bending elbows.",
            mistakes = listOf("Flaring elbows wide", "Arching lower back under load"),
            safety = "Maximizes stretch on tricep long head.",
            cues = "Keep elbows high and narrow, push hands away from neck.",
            progression = "Increase load when 12 reps are performed with deep stretch.",
            alternatives = listOf("skull_crusher", "cable_pushdown")
        ))

        list.add(createExercise(
            id = "skull_crusher",
            name = "Barbell Skull Crusher",
            altNames = listOf("Lying Triceps Extension"),
            primary = MuscleGroup.TRICEPS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.ISOLATION,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 90,
            instructions = "Lie on flat bench with EZ bar held over chest with arms angled back ~10 degrees. Lower bar toward forehead, then extend elbows back to start.",
            setup = "Lie flat on bench, elbows angled slightly behind shoulders rather than purely vertical.",
            execution = "Bend at elbows lowering bar toward hairline. Extend arms back up through triceps.",
            breathing = "Inhale lowering, exhale pressing up.",
            mistakes = listOf("Flaring elbows outward", "Letting bar hit forehead"),
            safety = "Use thumb-around grip and keep weight controlled.",
            cues = "Keep upper arms stationary, push hands toward ceiling.",
            progression = "Add 1.25 kg when 12 reps are achieved.",
            alternatives = listOf("cable_pushdown", "overhead_cable_extension")
        ))

        list.add(createExercise(
            id = "dips",
            name = "Parallel Bar Dips",
            altNames = listOf("Chest/Tricep Dips"),
            primary = MuscleGroup.TRICEPS,
            secondary = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
            movement = MovementPattern.VERTICAL_PUSH,
            equipment = EquipmentAvailable.BODYWEIGHT_HOME,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 120,
            instructions = "Grip parallel bars, support bodyweight with straight arms. Lower torso by bending elbows until upper arms are parallel to floor, press back up.",
            setup = "Upright torso emphasizes triceps (forward lean emphasizes chest).",
            execution = "Lower with control until 90-degree elbow bend is reached. Press through palms to lockout.",
            breathing = "Inhale on descent, exhale on press.",
            mistakes = listOf("Dropping too low causing shoulder strain", "Kicking legs wildly"),
            safety = "Do not dip deeper than shoulder mobility safely accommodates.",
            cues = "Press bars down into floor, stay upright for triceps.",
            progression = "Add weight belt once 12 bodyweight reps are mastered.",
            alternatives = listOf("machine_dip", "cable_pushdown")
        ))

        list.add(createExercise(
            id = "machine_dip",
            name = "Seated Machine Dip",
            altNames = listOf("Machine Tricep Dip"),
            primary = MuscleGroup.TRICEPS,
            secondary = listOf(MuscleGroup.CHEST),
            movement = MovementPattern.VERTICAL_PUSH,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 10, maxReps = 14, rir = 1, restSec = 75,
            instructions = "Sit in machine with back against pad, thighs belted or locked. Press handles down to full arm extension, return slowly.",
            setup = "Seat height adjusted so handles start at lower rib height.",
            execution = "Press handles down until triceps lock out. Squeeze for 1 second, control return.",
            breathing = "Exhale pressing down, inhale on return.",
            mistakes = listOf("Shrugging shoulders up into neck"),
            safety = "Gentler on shoulder joint than free dips.",
            cues = "Keep elbows pointing straight back, push down through heels of hands.",
            progression = "Increment pin weight by 5 kg upon reaching 14 reps.",
            alternatives = listOf("dips", "cable_pushdown")
        ))

        // 6. LEGS
        list.add(createExercise(
            id = "back_squat",
            name = "Barbell Back Squat",
            altNames = listOf("Squat", "Barbell Squat"),
            primary = MuscleGroup.LEGS,
            secondary = listOf(MuscleGroup.CORE),
            movement = MovementPattern.SQUAT,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 6, maxReps = 10, rir = 2, restSec = 180,
            instructions = "Rest barbell across upper traps (or rear delts). Descend by bending knees and hips until hip crease is below knee level, drive back up.",
            setup = "Feet slightly wider than shoulders, toes angled out 15-30 degrees. Brace core with 360-degree intra-abdominal pressure.",
            execution = "Sit hips down between knees while keeping chest proud. Reach parallel depth or lower. Drive floor away evenly through whole foot.",
            breathing = "Inhale deep diaphragmatic breath, brace Valsalva maneuver, descend, exhale past sticking point.",
            mistakes = listOf("Knees caving inward (valgus)", "Heels lifting off floor", "Rounding lower back at bottom (butt wink)"),
            safety = "Always set safety pins just below parallel depth in squat rack.",
            cues = "Spread the floor with your feet, screw knees out, drive upper back into the bar.",
            progression = "Add 2.5-5 kg when all sets hit 10 reps with solid depth.",
            alternatives = listOf("hack_squat", "leg_press")
        ))

        list.add(createExercise(
            id = "hack_squat",
            name = "Machine Hack Squat",
            altNames = listOf("Hack Squat"),
            primary = MuscleGroup.LEGS,
            secondary = listOf(MuscleGroup.CORE),
            movement = MovementPattern.SQUAT,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 150,
            instructions = "Position shoulders under pads with back flat against backrest. Feet shoulder-width on platform. Disengage safeties, squat deep, press back up.",
            setup = "Place feet mid-to-low on platform for quad emphasis. Keep back firmly planted against pad.",
            execution = "Descend with control until knees achieve full flexion without heels lifting. Press through mid-foot.",
            breathing = "Inhale down, exhale on ascent.",
            mistakes = listOf("Allowing lower back to peel away from pad", "Bouncing at bottom"),
            safety = "Engage safety catches before stepping off platform.",
            cues = "Track knees directly over toes, keep hips wedged into backrest.",
            progression = "Add plate weight when 12 reps are achieved with 2-second eccentric.",
            alternatives = listOf("back_squat", "leg_press")
        ))

        list.add(createExercise(
            id = "leg_press",
            name = "45-Degree Leg Press",
            altNames = listOf("Incline Leg Press"),
            primary = MuscleGroup.LEGS,
            secondary = emptyList(),
            movement = MovementPattern.SQUAT,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 10, maxReps = 14, rir = 2, restSec = 120,
            instructions = "Sit in machine with back and hips glued to pad. Place feet shoulder-width on sled. Lower sled under control until knees reach 90 degrees, press back up without locking knees.",
            setup = "Feet positioned shoulder-width in center of plate.",
            execution = "Lower sled smoothly toward chest. Stop before pelvis tucks under. Drive through midfoot.",
            breathing = "Inhale lowering, exhale pressing.",
            mistakes = listOf("Locking knees violently at top", "Rounding pelvis off seat at bottom", "Pushing knees with hands"),
            safety = "Never lock knees into hyperextension; keep soft bend at top.",
            cues = "Keep lower back pushed into seat, push the platform away.",
            progression = "Increase plate load once hitting 14 clean reps.",
            alternatives = listOf("hack_squat", "back_squat")
        ))

        list.add(createExercise(
            id = "bulgarian_split_squat",
            name = "Bulgarian Split Squat",
            altNames = listOf("Rear Foot Elevated Split Squat", "RFESS"),
            primary = MuscleGroup.LEGS,
            secondary = listOf(MuscleGroup.CORE),
            movement = MovementPattern.LUNGE,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.ADVANCED,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 90,
            instructions = "Rest top of rear foot on bench behind you. Holding dumbbells, lower back knee toward floor until front thigh is parallel to floor. Drive up through front heel.",
            setup = "Front foot about 2-3 feet ahead of bench. Torso tilted forward slightly to load front glute and quad.",
            execution = "Lower rear knee straight down toward floor. Press up through front foot without relying on back foot.",
            breathing = "Inhale down, exhale driving up.",
            mistakes = listOf("Putting too much weight on rear foot", "Front knee wobbling inward", "Stance too narrow"),
            safety = "Step out to comfortable distance to avoid excessive hip flexor strain.",
            cues = "All the work happens in the front leg. Drive through front heel.",
            progression = "Increase dumbbell weight when completing 12 reps per leg.",
            alternatives = listOf("lunges", "leg_press")
        ))

        list.add(createExercise(
            id = "lunges",
            name = "Walking Dumbbell Lunges",
            altNames = listOf("DB Lunges"),
            primary = MuscleGroup.LEGS,
            secondary = listOf(MuscleGroup.CORE),
            movement = MovementPattern.LUNGE,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 10, maxReps = 14, rir = 2, restSec = 90,
            instructions = "Hold dumbbells at sides. Step forward into lunge until both knees reach 90 degrees. Drive forward off front leg into next step.",
            setup = "Clear a walking path, hold dumbbells with neutral grip.",
            execution = "Step out, lower back knee smoothly to hover above floor. Step through smoothly.",
            breathing = "Steady breathing pattern synchronized with steps.",
            mistakes = listOf("Banging back knee hard on floor", "Walking with narrow tightrope footing"),
            safety = "Keep footsteps hip-width apart for knee stability.",
            cues = "Walk on railroad tracks, not a tightrope.",
            progression = "Add 2 kg per dumbbell when hitting 14 steps per leg.",
            alternatives = listOf("bulgarian_split_squat")
        ))

        list.add(createExercise(
            id = "leg_extension",
            name = "Leg Extension Machine",
            altNames = listOf("Seated Leg Extension"),
            primary = MuscleGroup.LEGS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 75,
            instructions = "Sit with knees aligned with machine pivot point. Lower shin pad above ankles. Extend legs until straight, squeeze quads for 1 second, lower under control.",
            setup = "Align knee joint directly with red pivot axis of machine. Pad sits just above foot.",
            execution = "Extend knees, pause at peak contraction. Lower with 3-second negative.",
            breathing = "Exhale extending, inhale lowering.",
            mistakes = listOf("Kicking weight up with momentum", "Lifting hips off seat"),
            safety = "Hold side handles firmly to keep hips pinned down.",
            cues = "Lock down into seat, squeeze quads hard at the top.",
            progression = "Increase pin weight after achieving 15 reps with 1-second hold.",
            alternatives = listOf("hack_squat")
        ))

        list.add(createExercise(
            id = "romanian_deadlift",
            name = "Barbell Romanian Deadlift (RDL)",
            altNames = listOf("RDL", "Stiff Leg Deadlift"),
            primary = MuscleGroup.LEGS,
            secondary = listOf(MuscleGroup.BACK, MuscleGroup.CORE),
            movement = MovementPattern.HINGE,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 150,
            instructions = "Stand holding bar with overhand grip. Soften knees, hinge hips back as if pushing a car door shut, lowering bar along shins until hamstrings stretch. Drive hips forward.",
            setup = "Feet hip-width, knees softly bent (unlocked but frozen). Shoulders pinned back.",
            execution = "Push hips directly backward. Bar stays glued to thighs and shins. Stop when hips stop moving back. Squeeze glutes to return.",
            breathing = "Inhale and brace core before hinge, exhale at top.",
            mistakes = listOf("Squatting the weight down with knees", "Rounding lumbar spine", "Allowing bar to drift away from legs"),
            safety = "Keep bar touching legs throughout movement.",
            cues = "Push your hips into the wall behind you, keep shins vertical.",
            progression = "Add 2.5-5 kg when 12 reps are completed with rigid spine.",
            alternatives = listOf("leg_curl", "seated_leg_curl")
        ))

        list.add(createExercise(
            id = "leg_curl",
            name = "Lying Leg Curl",
            altNames = listOf("Prone Leg Curl"),
            primary = MuscleGroup.LEGS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 10, maxReps = 14, rir = 1, restSec = 75,
            instructions = "Lie face down with shin pad resting above heels. Curl legs up toward glutes, pause, lower slowly with full hamstring stretch.",
            setup = "Align knee joints with machine pivot. Hold handles firmly.",
            execution = "Curl heels toward glutes. Hold 1 second. Lower under strict control.",
            breathing = "Exhale curling, inhale releasing.",
            mistakes = listOf("Lifting hips off the bench", "Swinging weight up"),
            safety = "Keep hips pressed flat into bench throughout.",
            cues = "Glue hips to pad, pull with your heels.",
            progression = "Add pin load once 14 reps with controlled eccentric are hit.",
            alternatives = listOf("seated_leg_curl", "romanian_deadlift")
        ))

        list.add(createExercise(
            id = "seated_leg_curl",
            name = "Seated Leg Curl",
            altNames = listOf("Seated Hamstring Curl"),
            primary = MuscleGroup.LEGS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 10, maxReps = 14, rir = 1, restSec = 75,
            instructions = "Sit with thigh pad locked over quads. Curl heels down and back under the seat, squeezing hamstrings at peak contraction.",
            setup = "Lock thigh pad down snugly so legs cannot lift.",
            execution = "Drive heels down and back. Control return until legs are almost fully extended.",
            breathing = "Exhale curling, inhale extending.",
            mistakes = listOf("Loose thigh pad allowing knees to rise", "Half reps"),
            safety = "Seated position places hamstrings in ideal stretched position.",
            cues = "Pull heels into the seat, control the stretch.",
            progression = "Increase pin weight after achieving 14 clean reps.",
            alternatives = listOf("leg_curl", "romanian_deadlift")
        ))

        list.add(createExercise(
            id = "hip_thrust",
            name = "Barbell Hip Thrust",
            altNames = listOf("Glute Bridge", "BB Hip Thrust"),
            primary = MuscleGroup.LEGS,
            secondary = listOf(MuscleGroup.CORE),
            movement = MovementPattern.HINGE,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.COMPOUND,
            sets = 3, minReps = 8, maxReps = 12, rir = 1, restSec = 120,
            instructions = "Sit on floor with upper back against bench, padded barbell across hips. Drive hips up to full lockout squeezing glutes, lower under control.",
            setup = "Bench height around knee level. Barbell pad over hip crease. Feet flat on floor with 90-degree knees at top.",
            execution = "Drive through heels, extending hips until torso and thighs form straight tabletop line. Posterior pelvic tilt at top.",
            breathing = "Inhale down, exhale driving hips up.",
            mistakes = listOf("Hyperextending lumbar spine instead of using glutes", "Looking up at ceiling instead of forward"),
            safety = "Always use a barbell pad to prevent bruising hip bones.",
            cues = "Keep chin tucked, drive through heels, lock glutes at top.",
            progression = "Add 5-10 kg when 12 reps with 1-sec hold are completed.",
            alternatives = listOf("romanian_deadlift")
        ))

        list.add(createExercise(
            id = "calf_raise",
            name = "Standing Calf Raise",
            altNames = listOf("Machine Calf Raise"),
            primary = MuscleGroup.LEGS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 4, minReps = 12, maxReps = 16, rir = 1, restSec = 60,
            instructions = "Place balls of feet on block with heels hanging off. Raise up onto toes as high as possible, pause, lower to deep calf stretch.",
            setup = "Shoulder pads secure, knees straight but not hyper-locked.",
            execution = "Rise onto big toes, squeeze calves for 2 seconds. Descend to full stretch and pause 1 second to eliminate Achilles reflex.",
            breathing = "Exhale up, inhale down.",
            mistakes = listOf("Bouncing rapidly using tendon elasticity", "Half reps"),
            safety = "Pausing at bottom ensures muscle fibers do the work, not tendon bounce.",
            cues = "Drive through big toes, pause at bottom stretch.",
            progression = "Increase weight after achieving 16 reps with strict 2-sec top squeeze.",
            alternatives = listOf("seated_calf_raise")
        ))

        list.add(createExercise(
            id = "seated_calf_raise",
            name = "Seated Calf Raise",
            altNames = listOf("Soleus Calf Raise"),
            primary = MuscleGroup.LEGS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.MACHINE,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 60,
            instructions = "Sit with knee pad locked across thighs. Place balls of feet on step. Raise heels, hold squeeze, lower to full stretch for soleus development.",
            setup = "Lock pad snugly across lower thighs.",
            execution = "Full range of motion: deep stretch at bottom, full contraction at top.",
            breathing = "Exhale raising, inhale lowering.",
            mistakes = listOf("Bouncing the weight"),
            safety = "Gentle on knees; targets deep soleus calf muscle.",
            cues = "Slow 2-second ascent, 2-second descent.",
            progression = "Add 2.5-5 kg when 15 reps are met.",
            alternatives = listOf("calf_raise")
        ))

        // 7. CORE
        list.add(createExercise(
            id = "cable_crunch",
            name = "Kneeling Cable Crunch",
            altNames = listOf("Rope Cable Crunch"),
            primary = MuscleGroup.CORE,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.FULL_GYM,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.CABLE,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 60,
            instructions = "Kneel facing cable pulley with rope held at forehead. Flex spine to crunch elbows down toward knees, squeeze abs, return with control.",
            setup = "Kneel about 2 feet from stack. Lock hips in position.",
            execution = "Curl torso forward by curling ribs toward hips. Do not sit back on heels.",
            breathing = "Exhale forcefully on crunch, inhale on return.",
            mistakes = listOf("Hinging at hips instead of curling spine", "Sitting back onto heels"),
            safety = "Maintain fixed hip angle so abdominals perform the spinal flexion.",
            cues = "Roll your ribs down to your pelvis like rolling a carpet.",
            progression = "Increase pin weight after achieving 15 clean crunches.",
            alternatives = listOf("hanging_knee_raise", "leg_raise")
        ))

        list.add(createExercise(
            id = "hanging_knee_raise",
            name = "Hanging Knee Raise",
            altNames = listOf("Captain's Chair Knee Raise"),
            primary = MuscleGroup.CORE,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.BODYWEIGHT_HOME,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.BODYWEIGHT,
            sets = 3, minReps = 10, maxReps = 15, rir = 1, restSec = 60,
            instructions = "Hang from pull-up bar. Without swinging, lift knees toward chest by curling pelvis upward. Lower slowly.",
            setup = "Dead hang or forearm support in captain's chair.",
            execution = "Roll pelvis upward and bring knees toward chest. Control descent without swinging.",
            breathing = "Exhale raising knees, inhale lowering.",
            mistakes = listOf("Swinging body for momentum", "Only lifting thighs without curling pelvis"),
            safety = "Do not swing backward; keep core engaged throughout.",
            cues = "Curl your belt buckle up toward your chin.",
            progression = "Progress to straight-leg hanging raises as strength increases.",
            alternatives = listOf("leg_raise", "cable_crunch")
        ))

        list.add(createExercise(
            id = "leg_raise",
            name = "Lying Leg Raise",
            altNames = listOf("Floor Leg Raise"),
            primary = MuscleGroup.CORE,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.BODYWEIGHT_HOME,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.BODYWEIGHT,
            sets = 3, minReps = 12, maxReps = 16, rir = 1, restSec = 60,
            instructions = "Lie on back with hands under glutes. Keep legs straight, raise legs to 90 degrees, lower slowly until heels hover 1 inch off floor.",
            setup = "Lower back pressed firmly into floor.",
            execution = "Raise legs vertically, lift tailbone slightly at top, lower slowly without arching lower back.",
            breathing = "Exhale raising legs, inhale lowering.",
            mistakes = listOf("Arching lower back off floor", "Using momentum"),
            safety = "If lower back lifts, bend knees slightly.",
            cues = "Crush a grape under your lower back against the floor.",
            progression = "Add small ankle weights when 16 strict reps are achieved.",
            alternatives = listOf("hanging_knee_raise", "plank")
        ))

        list.add(createExercise(
            id = "plank",
            name = "Standard Forearm Plank",
            altNames = listOf("Front Plank"),
            primary = MuscleGroup.CORE,
            secondary = listOf(MuscleGroup.SHOULDERS),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.BODYWEIGHT_HOME,
            difficulty = Difficulty.BEGINNER,
            type = ExerciseType.BODYWEIGHT,
            sets = 3, minReps = 30, maxReps = 60, rir = 1, restSec = 60,
            instructions = "Rest on forearms and toes with body in straight line. Squeeze glutes, brace abs, hold without sagging or piking hips.",
            setup = "Elbows directly under shoulders, feet hip-width.",
            execution = "Create whole-body tension: clench fists, squeeze quads, brace abs.",
            breathing = "Controlled diaphragmatic breathing while maintaining brace.",
            mistakes = listOf("Sagging hips hyperextending back", "Hiking hips up like a tent", "Holding breath"),
            safety = "Stop immediately if feeling discomfort in lower back.",
            cues = "Pull elbows toward toes to engage maximum tension.",
            progression = "Progress by extending hold time from 30 to 60 seconds or elevating feet.",
            alternatives = listOf("leg_raise", "cable_crunch")
        ))

        list.add(createExercise(
            id = "incline_dumbbell_curl",
            name = "Incline Dumbbell Curl",
            altNames = listOf("Incline Bicep Curl"),
            primary = MuscleGroup.BICEPS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.ISOLATION,
            sets = 3, minReps = 10, maxReps = 12, rir = 1, restSec = 75,
            instructions = "Sit back on a 45-60 degree incline bench with dumbbells hanging straight down. Curl upward while keeping elbows back for deep long-head stretch.",
            setup = "Set incline to 45-60 degrees. Back flush against pad, arms hanging fully extended perpendicular to floor.",
            execution = "Curl dumbbells while keeping elbows pinned back in space. Supinate wrists at top, lower under full 3-second stretch.",
            breathing = "Exhale curling, inhale lowering.",
            mistakes = listOf("Swinging elbows forward turning it into a front raise", "Arching back off bench"),
            safety = "Avoid excessive shoulder hyper-extension at bottom.",
            cues = "Keep upper arms vertical, let the bicep stretch deeply at the bottom.",
            progression = "Advance to heavier dumbbell pair when 12 reps are achieved with 3-second eccentric.",
            alternatives = listOf("dumbbell_curl", "barbell_curl")
        ))

        list.add(createExercise(
            id = "standing_single_leg_calf_raise",
            name = "Single-Leg Dumbbell Calf Raise",
            altNames = listOf("One-Leg Calf Raise"),
            primary = MuscleGroup.LEGS,
            secondary = emptyList(),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.INTERMEDIATE,
            type = ExerciseType.ISOLATION,
            sets = 3, minReps = 12, maxReps = 15, rir = 1, restSec = 60,
            instructions = "Hold dumbbell in one hand, place ball of same-side foot on edge of step. Raise heel as high as possible, hold for 1 second, lower to full calf stretch.",
            setup = "Ball of working foot on sturdy ledge or plate, other hand lightly touching wall for balance.",
            execution = "Drive through big toe to full plantarflextion. Pause 1 second at top, descend with 3-second tempo.",
            breathing = "Exhale on rise, inhale on descent.",
            mistakes = listOf("Bouncing out of bottom stretch", "Bent knee shifting emphasis to soleus"),
            safety = "Use wall for light balance only, not to pull yourself up.",
            cues = "Pause at bottom to remove Achilles tendon bounce.",
            progression = "Increase dumbbell weight when 15 clean reps per leg are completed.",
            alternatives = listOf("calf_raise", "seated_calf_raise")
        ))

        list.add(createExercise(
            id = "ab_wheel_rollout",
            name = "Ab Wheel Rollout",
            altNames = listOf("Kneeling Ab Rollout"),
            primary = MuscleGroup.CORE,
            secondary = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BACK),
            movement = MovementPattern.ISOLATION,
            equipment = EquipmentAvailable.BODYWEIGHT_HOME,
            difficulty = Difficulty.ADVANCED,
            type = ExerciseType.BODYWEIGHT,
            sets = 3, minReps = 8, maxReps = 12, rir = 2, restSec = 75,
            instructions = "Kneel on mat with hands on ab wheel. Roll wheel forward while bracing core and maintaining slight posterior pelvic tilt, pull back with abdominals.",
            setup = "Knees hip-width on padded mat, ab wheel directly beneath shoulders.",
            execution = "Roll wheel forward slowly as far as can be controlled without lower back sagging. Pull back using core tension.",
            breathing = "Inhale rolling out, exhale pulling back.",
            mistakes = listOf("Sagging lower back into hyperextension", "Leading with hips when pulling back"),
            safety = "Only roll as far as you can maintain abdominal brace without lumbar extension.",
            cues = "Tuck pelvis slightly, pull with your abs like closing a jackknife.",
            progression = "Increase rollout distance or progress to standing rollouts.",
            alternatives = listOf("plank", "cable_crunch")
        ))

        return list
    }

    private fun createExercise(
        id: String,
        name: String,
        altNames: List<String>,
        primary: MuscleGroup,
        secondary: List<MuscleGroup>,
        movement: MovementPattern,
        equipment: EquipmentAvailable,
        difficulty: Difficulty,
        type: ExerciseType,
        sets: Int,
        minReps: Int,
        maxReps: Int,
        rir: Int,
        restSec: Int,
        instructions: String,
        setup: String,
        execution: String,
        breathing: String,
        mistakes: List<String>,
        safety: String,
        cues: String,
        progression: String,
        alternatives: List<String>
    ): ExerciseEntity {
        return ExerciseEntity(
            id = id,
            name = name,
            alternativeNames = altNames,
            primaryMuscle = primary.name,
            secondaryMuscles = secondary,
            movementPattern = movement.name,
            equipment = equipment.name,
            difficulty = difficulty.name,
            exerciseType = type.name,
            recommendedSets = sets,
            minReps = minReps,
            maxReps = maxReps,
            rirTarget = rir,
            restTimeSeconds = restSec,
            instructions = instructions,
            setupInstructions = setup,
            executionInstructions = execution,
            breathingInstructions = breathing,
            commonMistakes = mistakes,
            safetyNotes = safety,
            coachingCues = cues,
            progressionNotes = progression,
            alternativeExerciseIds = alternatives,
            status = ContentStatus.PUBLISHED.name,
            isFavorite = false,
            viewsCount = 10,
            completionCount = 5,
            replacementCount = 0,
            lastPerformedWeightKg = null,
            lastPerformedReps = null,
            lastPerformedDateMillis = null
        )
    }

    fun getInitialMedia(): List<ExerciseMediaEntity> {
        val list = mutableListOf<ExerciseMediaEntity>()
        val exercises = getInitialExercises()

        for (ex in exercises) {
            // Video media
            list.add(
                ExerciseMediaEntity(
                    mediaId = "${ex.id}_video_1",
                    exerciseId = ex.id,
                    type = MediaType.VIDEO.name,
                    url = "https://assets.formax.app/videos/${ex.id}_guide.mp4",
                    localPath = "exercises/videos/${ex.id}.mp4",
                    thumbnail = "exercises/thumbnails/${ex.id}_thumb.webp",
                    title = "${ex.name} Technique Video Guide",
                    description = "Professional demonstration covering setup, range of motion, and common faults.",
                    durationSeconds = 48,
                    source = "local_asset",
                    isPrimary = true,
                    sortOrder = 1,
                    language = "en",
                    createdAtMillis = System.currentTimeMillis(),
                    updatedAtMillis = System.currentTimeMillis()
                )
            )

            // GIF/Animation media
            list.add(
                ExerciseMediaEntity(
                    mediaId = "${ex.id}_gif_1",
                    exerciseId = ex.id,
                    type = MediaType.GIF.name,
                    url = "https://assets.formax.app/animations/${ex.id}_loop.gif",
                    localPath = "exercises/animations/${ex.id}.gif",
                    thumbnail = "",
                    title = "${ex.name} Movement Loop",
                    description = "Continuous loop illustrating start, eccentric transition, and concentric lockout.",
                    durationSeconds = 4,
                    source = "local_asset",
                    isPrimary = false,
                    sortOrder = 2,
                    language = "en",
                    createdAtMillis = System.currentTimeMillis(),
                    updatedAtMillis = System.currentTimeMillis()
                )
            )

            // Image media: Starting position
            list.add(
                ExerciseMediaEntity(
                    mediaId = "${ex.id}_img_start",
                    exerciseId = ex.id,
                    type = MediaType.IMAGE.name,
                    url = "https://assets.formax.app/images/${ex.id}_start.webp",
                    localPath = "exercises/images/${ex.id}_start.webp",
                    thumbnail = "",
                    title = "Starting Setup",
                    description = "Proper joint alignment and initial brace.",
                    durationSeconds = 0,
                    source = "local_asset",
                    isPrimary = false,
                    sortOrder = 3,
                    language = "en",
                    createdAtMillis = System.currentTimeMillis(),
                    updatedAtMillis = System.currentTimeMillis()
                )
            )

            // Image media: Finish position
            list.add(
                ExerciseMediaEntity(
                    mediaId = "${ex.id}_img_finish",
                    exerciseId = ex.id,
                    type = MediaType.IMAGE.name,
                    url = "https://assets.formax.app/images/${ex.id}_finish.webp",
                    localPath = "exercises/images/${ex.id}_finish.webp",
                    thumbnail = "",
                    title = "Peak Contraction",
                    description = "Target muscle locked in full contraction.",
                    durationSeconds = 0,
                    source = "local_asset",
                    isPrimary = false,
                    sortOrder = 4,
                    language = "en",
                    createdAtMillis = System.currentTimeMillis(),
                    updatedAtMillis = System.currentTimeMillis()
                )
            )
        }

        return list
    }
}
