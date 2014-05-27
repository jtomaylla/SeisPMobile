package org.ses.android.soap.database;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Visitas implements KvmSerializable {

	public String Proyecto;
	public String Visita;
	public String FechaVisita;
	public String HoraCita;
	public String EstadoVisita;
	
	public Visitas()
	{
		Proyecto = "";
		Visita = "";
		FechaVisita = "";
		HoraCita = "";
		EstadoVisita= "";
	}
	
	public Visitas(
			String Proyecto,
			String Visita,
			String FechaVisita,
			String HoraCita,
			String EstadoVisita
			)
	{
		this.Proyecto = Proyecto;
		this.Visita = Visita;
		this.FechaVisita = FechaVisita;
		this.HoraCita = HoraCita;
		this.EstadoVisita  = EstadoVisita ;
	}
	
	@Override
	public Object getProperty(int arg0) {

		switch(arg0)
        {
        case 0:
            return Proyecto;
        case 1:
            return Visita;
        case 2:
            return FechaVisita;
        case 3:
            return HoraCita;
        case 4:
            return EstadoVisita;
        }
		
		return null;
	}
	
	@Override
	public int getPropertyCount() {
		return 5;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void  getPropertyInfo(int ind, Hashtable ht, PropertyInfo info) {
		switch(ind)
        {
        case 0:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Proyecto";
            break;
        case 1:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Visita";
            break;
        case 2:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "FechaVisita";
            break;
        case 3:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "HoraCita";
            break;
        case 4:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "EstadoVisita";
            break;
	            
	    default:break;
        }
	}
	
	@Override
	public void  setProperty(int ind, Object val) {
		switch(ind)
        {
        case 0:
        	Proyecto  = val.toString();
            break;
        case 1:
        	Visita  = val.toString();
            break;
        case 2:
        	FechaVisita = val.toString();
            break;
        case 3:
        	HoraCita  = val.toString();
            break;
        case 4:
        	EstadoVisita  = val.toString();
            break;

	    default:break;
        }
	}
}
