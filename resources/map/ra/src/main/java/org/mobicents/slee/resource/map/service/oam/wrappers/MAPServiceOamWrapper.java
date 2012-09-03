/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.map.service.oam.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPServiceOam;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPServiceOamListener;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.wrappers.MAPProviderWrapper;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPServiceOamWrapper implements MAPServiceOam {

	protected MAPServiceOam wrappedOam;
	protected MAPProviderWrapper mapProviderWrapper;

	/**
	 * @param mapServiceOam
	 */
	public MAPServiceOamWrapper(MAPProviderWrapper mapProviderWrapper, MAPServiceOam mapOam) {
		this.wrappedOam = mapOam;
		this.mapProviderWrapper = mapProviderWrapper;
	}

	public void acivate() {
		throw new UnsupportedOperationException();
	}

	public void deactivate() {
		throw new UnsupportedOperationException();
	}

	public MAPProvider getMAPProvider() {
		return this.mapProviderWrapper;
	}

	public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
		return this.wrappedOam.isServingService(dialogApplicationContext);
	}

	public boolean isActivated() {
		return this.wrappedOam.isActivated();
	}

	public void addMAPServiceListener(MAPServiceOamListener arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MAPDialogOam createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
			AddressString destReference) throws MAPException {
		MAPDialogOam mapDialog = this.wrappedOam.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference);
		MAPDialogActivityHandle activityHandle = new MAPDialogActivityHandle(mapDialog.getDialogId());
		MAPDialogOamWrapper dw = new MAPDialogOamWrapper(mapDialog, activityHandle, this.mapProviderWrapper.getRa());
		mapDialog.setUserObject(dw);

		try {
			this.mapProviderWrapper.getRa().startSuspendedActivity(dw);
		} catch (Exception e) {
			throw new MAPException(e);
		}

		return dw;
	}

	public void removeMAPServiceListener(MAPServiceOamListener arg0) {
		throw new UnsupportedOperationException();
	}
}