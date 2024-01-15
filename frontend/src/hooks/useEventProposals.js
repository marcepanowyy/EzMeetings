import { useEffect } from 'react';

const useEventProposals = (
    event,
    setProposals,
    locationChanged,
    descriptionChanged,
    nameChanged
) => {
    useEffect(() => {
        if (event?.eventProposals) {
            const initialProposals = event.eventProposals.map((proposal) => ({
                id: proposal.id,
                start: new Date(proposal.startDate),
                end: new Date(
                    new Date(proposal.startDate).setHours(
                        new Date(proposal.startDate).getHours() + 1
                    )
                ),
                title: event.name,
            }));
            setProposals(initialProposals);
            locationChanged(event?.location || '');
            descriptionChanged(event?.description || '');
            nameChanged(event?.name || '');
        }
    }, []);
};

export default useEventProposals;
