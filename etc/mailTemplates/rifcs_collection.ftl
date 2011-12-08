<registryObject group="Monash University">
	<key>${HdlIdentifier}</key>
	<originatingSource>${AppName}</originatingSource>
	<collection type="collection">
		<identifier type="local">${MonUUID}</identifier>
		<name type="primary">
			<namePart>${CollectionName}</namePart>
		</name>
		<location>
			<address>
				<electronic type="url">
					<value>${ElectronicUrl}</value>
				</electronic>
				<physical>
					<addressPart type="text">
						${PhysicalAddress}
					</addressPart>
				</physical>
			</address>
		</location>
		<coverage>
			<spatial type="kmlPolyCoords">${SpatialCoverage}</spatial>
			<temporal>
				<date type="dateFrom" dateFormat="UTC">${DateFrom}</date>
				<date type="dateTo" dateFormat="UTC">${DateTo}</date>
			</temporal>
		</coverage>
		${RelatedParties}
		${RelatedActivities}
		<subject type="anzsrc-for">${AnzSrcFor}</subject>
		<description type="rights" xml:lang="en">
			${Righsts}
		</description>
		<description type="brief" xml:lang="en">
			${Desciptions}
		</description>
	</collection>
</registryObject>
	