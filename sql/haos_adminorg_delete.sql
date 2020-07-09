DELETE FROM T_HAOS_ADMINORG WHERE FID >= '#adminorgid1' and FID <= '#adminorgid2';
DELETE FROM T_HAOS_ADMINORG_L WHERE FID >= '#adminorgid1' and FID <= '#adminorgid2';
DELETE FROM T_HAOS_ADMINSTRUCT WHERE FID >= '#adminorgid1' and FID <= '#adminorgid2';
DELETE FROM t_hbss_empstrategy WHERE FID>= '#adminorgid1' and FID <= '#adminorgid2';
DELETE FROM t_hbss_empstrentry WHERE FID>= '#adminorgid1' and FID <= '#adminorgid2';
DELETE FROM t_haos_defstratgy WHERE FID>= '#adminorgid1' and FID <= '#adminorgid2';
