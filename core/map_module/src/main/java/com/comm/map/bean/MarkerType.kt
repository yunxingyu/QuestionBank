package com.comm.map.bean

enum class MarkerType(val value: String) {
    HOME_MARKER("home_marker"),
    RISE_MARKER("rise_marker"),
    DECLINE_MARKER("decline_marker"),
    PHONE_MARKER("remote_marker"),
    FORCE_LANDING_MARKER("force_landing_marker"),
    POI_MARKER("poi_marker"),
    DRONE_MARKER("drone_marker"),
    WAYPOINT_MARKER("waypoint_marker"),
    WAYPOINT_HEIGHT_MARKER("waypoint_height_marker"),
    WAYPOINT_HEADING_MARKER("waypoint_heading_marker"),
    WAYPOINT_INSERT_MARKER("waypoint_insert_marker"),
    START_AVOID("start_avoid"),
    END_AVOID("end_avoid"),
    START_INSERT("start_insert"),
    END_INSERT("end_insert"),
    ORBIT_CIRCLE_CONTROL("orbit_circle_control"),
    ORBIT_ROTATE_CONTROL("orbit_rotate_control"),
    ARROW_MARKER("arrow_marker"),
    MAPPING("mapping"),
    MAPPING_WAYPOINT("mapping_waypoint"),
    MAPPING_VERTEX("mapping_vertex"),
    MAPPING_INSERT("mapping_insert"),
    MAPPING_INTEREST("mapping_interest"),
    NOT_FLY_ZONE("not_fly_zone"),
    NOT_FLY_ZONE_VERTEX("not_fly_zone_vertex"),
    DSP_TARGET("dsp_target"),
    NOT_FLY_ZONE_INSERT("not_fly_zone_insert"),
    DRAG_CENTER("drag_center"),
    LEFT_ROTATE("left_rotate"),
    RIGHT_ROTATE("right_rotate"),
    START("start"),
    END("end"),
    PAUSE_POINT("pausePoint"),
    FLIGHT_LOG_CAMERA("FLIGHT_LOG_CAMERA"),
    FIND_DRONE_LOCATION("find_drone_location"),
    ORBIT_MARKER("orbit_marker"),
    BREAK_POINT("break_point"),
    ADSB_MARKER("adsb_marker"),
    UNKNOWN("unknown_mission");

    fun value(): String {
        return value
    }

    companion object {
        fun find(value: String): MarkerType {
            if (HOME_MARKER.value() == value) {
                return HOME_MARKER
            }
            if (RISE_MARKER.value() == value) {
                return RISE_MARKER
            }
            if (DECLINE_MARKER.value() == value) {
                return DECLINE_MARKER
            }
            if (PHONE_MARKER.value() == value) {
                return PHONE_MARKER
            }
            if (DRONE_MARKER.value() == value) {
                return DRONE_MARKER
            }
            if (WAYPOINT_MARKER.value() == value) {
                return WAYPOINT_MARKER
            }
            if (WAYPOINT_INSERT_MARKER.value() == value) {
                return WAYPOINT_INSERT_MARKER
            }
            if (MAPPING.value() == value) {
                return MAPPING
            }
            if (MAPPING_WAYPOINT.value() == value) {
                return MAPPING_WAYPOINT
            }
            if (MAPPING_VERTEX.value() == value) {
                return MAPPING_VERTEX
            }
            if (MAPPING_INSERT.value() == value) {
                return MAPPING_INSERT
            }
            if (MAPPING_INTEREST.value() == value) {
                return MAPPING_INTEREST
            }
            if (NOT_FLY_ZONE.value() == value) {
                return NOT_FLY_ZONE
            }
            if (NOT_FLY_ZONE_VERTEX.value() == value) {
                return NOT_FLY_ZONE_VERTEX
            }
            if (NOT_FLY_ZONE_INSERT.value() == value) {
                return NOT_FLY_ZONE_INSERT
            }
            if (START_AVOID.value() == value) {
                return START_AVOID
            }
            if (END_AVOID.value() == value) {
                return END_AVOID
            }
            if (START_INSERT.value() == value) {
                return START_INSERT
            }
            if (END_INSERT.value() == value) {
                return END_INSERT
            }
            if (ORBIT_CIRCLE_CONTROL.value() == value) {
                return ORBIT_CIRCLE_CONTROL
            }
            if (ORBIT_ROTATE_CONTROL.value() == value) {
                return ORBIT_ROTATE_CONTROL
            }
            if (DRAG_CENTER.value() == value) {
                return DRAG_CENTER
            }
            if (LEFT_ROTATE.value() == value) {
                return LEFT_ROTATE
            }
            if (RIGHT_ROTATE.value() == value) {
                return RIGHT_ROTATE
            }
            if (START.value() == value) {
                return START
            }
            if (END.value() == value) {
                return END
            }
            if (WAYPOINT_HEIGHT_MARKER.value() == value) {
                return WAYPOINT_HEIGHT_MARKER
            }
            if (WAYPOINT_HEADING_MARKER.value() == value) {
                return WAYPOINT_HEADING_MARKER
            }
            if (PAUSE_POINT.value() == value) {
                return PAUSE_POINT
            }
            if (FIND_DRONE_LOCATION.value() == value) {
                return FIND_DRONE_LOCATION
            }
            if (ORBIT_MARKER.value() == value) {
                return ORBIT_MARKER
            }
            return if (ADSB_MARKER.value() == value) {
                ADSB_MARKER
            } else UNKNOWN
        }
    }
}