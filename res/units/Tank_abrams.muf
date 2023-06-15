base
	name=Tank abrams;
	description=Американский танк абрамс;
	health=1400;
	max_speed=80;
	baseModel=/models/t_abrams.obj;
	flags=NO_MACHINE_GUN, BETTER_ARMOR_UPDATE;
	unitType=UNIT_TANK;
	primaryWeapon=ABRAMS_PRIMARY;
end

eventHandler

	event CRITICAL_DAMAGE
		primaryWeapon=ABRAMS_PRIMARY_D;
		baseModel=/models/t_abrams.obj;
	end

	event LEVEL_2
		primaryWeapon=ABRAMS_PRIMARY_LV2;
	end


end

