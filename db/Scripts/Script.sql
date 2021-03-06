SELECT TX.ID,TX.CREATED,TX.DELETED,TX.REFERENCE,TX.NARRATIVE,TX.TDATE,
FE.ID AS "FROM_ID",FE.DELETED AS "FROM_DELETED",FE.AMOUNT AS "FROM_AMOUNT",FE.ETYPE AS "FROM_ETYPE",FE.ACCOUNT_ID AS "FROM_ACCOUNT_ID",FE.CLEARED AS "FROM_CLEARED",FE.CLEARED_TS as "FROM_CLEARED_TS",FE.CREATED AS "FROM_CREATED",
TE.ID AS "TO_ID",TE.DELETED AS "TO_DELETED",TE.AMOUNT AS "TO_AMOUNT",TE.ETYPE AS "TO_ETYPE",TE.ACCOUNT_ID AS "TO_ACCOUNT_ID",TE.CLEARED AS "TO_CLEARED",TE.CLEARED_TS AS "TO_CLEARED_TS",TE.CREATED AS "TO_CREATED"
 FROM DEBS.TRANSACTION AS TX
 INNER JOIN DEBS.ENTRY AS FE ON FE.ID=TX.EID_FROM AND FE.ETYPE=1
 INNER JOIN DEBS.ENTRY AS TE ON TE.ID=TX.EID_TO AND TE.ETYPE=2
 WHERE ((FE.ACCOUNT_ID=? AND TX.DELETED=FALSE) OR (TE.ACCOUNT_ID=? AND TX.DELETED=FALSE)) AND WHERE YEAR(CREATED)=? AND MONTH(CREATED)=?