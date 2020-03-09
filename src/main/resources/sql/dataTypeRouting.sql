select data_type_mapping.data_type
  from time_series_description
       left join data_type_mapping
         on time_series_description.parm_cd = data_type_mapping.parm_cd and
            time_series_description.stat_cd = data_type_mapping.stat_cd
 where time_series_description.time_series_unique_id = ?
